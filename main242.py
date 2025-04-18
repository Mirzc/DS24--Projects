import os
import json
import csv
import xml.etree.ElementTree as ET
from collections import defaultdict, Counter
from datetime import datetime
import matplotlib.pyplot as plt

def get_manual_entry():
    entry_type = input("Type of entry (Workout/Meal): ").capitalize()
    while entry_type not in ["Workout", "Meal"]:
        print("Invalid type. Please enter 'Workout' or 'Meal'.")
        entry_type = input("Type of entry (Workout/Meal): ").capitalize()

    category = input("Category (e.g., Cardio, Lunch): ").strip()
    duration = input("Duration/Quantity (e.g., 30 minutes, 1 plate): ").strip()

    try:
        calories = int(input("Calories (burned/consumed): ").strip())
    except ValueError:
        print("Invalid input for calories. Must be a number.")
        return None

    date = input("Date (YYYY-MM-DD): ").strip()

    entry = {
        "type": entry_type,
        "category": category,
        "duration": duration,
        "calories": calories,
        "date": date
    }
    return entry


def import_entries_from_csv(filepath):
    entries = []
    try:
        with open(filepath, mode='r') as file:
            reader = csv.DictReader(file)
            for row in reader:
                try:
                    entry = {
                        "type": row["type"].capitalize(),
                        "category": row["category"],
                        "duration": row["duration"],
                        "calories": int(row["calories"]),
                        "date": row["date"]
                    }
                    entries.append(entry)
                except (KeyError, ValueError) as e:
                    print(f"Skipping invalid row: {row} -> {e}")
    except FileNotFoundError:
        print("CSV file not found.")
    return entries


def import_entries_from_xml(filepath):
    entries = []
    try:
        tree = ET.parse(filepath)
        root = tree.getroot()
        for item in root.findall("entry"):
            try:
                entry = {
                    "type": item.find("type").text.capitalize(),
                    "category": item.find("category").text,
                    "duration": item.find("duration").text,
                    "calories": int(item.find("calories").text),
                    "date": item.find("date").text
                }
                entries.append(entry)
            except (AttributeError, ValueError) as e:
                print(f"Skipping invalid XML entry: {e}")
    except FileNotFoundError:
        print("XML file not found.")
    return entries

def add_entry_to_user(entry, user_data):
    if entry:
        user_data["entries"].append(entry)
        print(" Entry added successfully.")


def load_user_data(username):
    filename = f"data/{username}.json"
    if os.path.exists(filename):
        with open(filename, "r") as file:
            return json.load(file)
    else:
        print(f"Creating new profile for {username}")
        return {
            "weight": input("Enter your weight (kg): "),
            "height": input("Enter your height (cm): "),
            "goal": input("Enter your fitness goal: "),
            "entries": []
        }


def save_user_data(username, user_data):
    os.makedirs("data", exist_ok=True)
    with open(f"data/{username}.json", "w") as file:
        json.dump(user_data, file, indent=4)
    print(" User data saved successfully.")

def edit_user_profile(user_data):
    print("\n   Edit Profile   ")
    print(f"Current weight: {user_data['weight']}")
    print(f"Current height: {user_data['height']}")
    print(f"Current goal: {user_data['goal']}")

    new_weight = input("Please enter New weight or press enter to keep current").strip()
    new_height = input("Please enter New height or press enter to keep current").strip()
    new_goal = input("Please enter New goal or press enter to keep current").strip()

    if new_weight:
                    user_data["weight"] = new_weight
    if new_height:
                    user_data["height"] = new_height
    if new_goal:
                    user_data["goal"] = new_goal

                    print("Profile Updated")
                    
def visCalTrend(entries):
    daily_data = defaultdict(lambda: {"burned": 0, "consumed": 0})
    
    for entry in entries:
        date = entry["date"]
        calories = entry["calories"]
        if entry["type"] == "Workout":
            daily_data[date]["burned"] += calories
        elif entry["type"] == "Meal":
            daily_data[date]["consumed"] += calories
    
    sorted_dates = sorted(daily_data.keys(), key=lambda x: datetime.strptime(x, "%Y-%m-%d"))
    
    dates = []
    burned = []
    consumed = []
    
    for date in sorted_dates:
        dates.append(date)
        burned.append(daily_data[date]["burned"])
        consumed.append(daily_data[date]["consumed"])
        
    plt.figure(figsize=(10, 5))
    plt.plot(dates, burned, marker='o', label='Calories Burned')
    plt.plot(dates, consumed, marker='o', label='Calories Consumed')
    plt.xlabel("Date")
    plt.ylabel("Calories")
    plt.title("Calories Burned vs. Consumed Over Time")
    plt.xticks(rotation=45)
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    plt.show()
    
    
def visCatDist(entries):
    print("\nCategory Distribution")
    print("1. Workout Categorie")
    print("2. Meal Categorie")

    choice = input("Enter A Choice 1 or 2: ").strip()

    if choice == "1":
        entry_type = "Workout"
        title = "Workout Category Distribution"
    elif choice == "2":
        entry_type = "Meal"
        title = "Meal Category Distribution"
    else:
        print("Wrong Entry.")
        return

    filtered = []
    for entry in entries:
        if entry["type"] == entry_type:
            filtered.append(entry["category"])

    if not filtered:
        print("No entries found for selected type.")
        return
    counts = Counter(filtered)
    categories = list(counts.keys())
    values = list(counts.values())
    
    plt.figure(figsize=(8, 5))
    plt.bar(categories, values)
    plt.xlabel("Category")
    plt.ylabel("Number of Entries")
    plt.title(title)
    plt.xticks(rotation=30)
    plt.tight_layout()
    plt.grid(axis='y')
    plt.show()
    
def main():
    print(" Fitness & Nutrition Tracker ")
    username = input("Enter your username: ")
    user_data = load_user_data(username)

    while True:
        print("\nChoose an option:")
        print("1. Add entry manually")
        print("2. Import entries from CSV")
        print("3. Import entries from XML")
        print("4. View all entries")
        print("5. Visualize your data")
        print("6. Edit Profile")
        print("7. Save and Exit")


        choice = input("Your choice: ")

        if choice == "1":
            entry = get_manual_entry()
            add_entry_to_user(entry, user_data)

        elif choice ==  "2":
            path = input("Enter CSV file path: ")
            entries = import_entries_from_csv(path)
            for entry in entries:
                add_entry_to_user(entry, user_data)

        elif choice == "3":
            path = input("Enter XML file path: ")
            entries = import_entries_from_xml(path)
            for entry in entries:
                add_entry_to_user(entry, user_data)

        elif choice == "4":
            print("\n All Entries ")
            for i, entry in enumerate(user_data["entries"], 1):
                print(f"\nEntry {i}:")
                print(f"  Type -Workout/Meal-      : {entry['type']}")
                print(f"  Category -Cardio/Lunch-  : {entry['category']}")
                print(f"  Duration/Quantity        : {entry['duration']}")
                print(f"  Calories Burned/Consumed : {entry['calories']}")
                print(f"  Date yyyy-mm-dd          : {entry['date']}")
            
        elif choice == "5":
            print("1. Calories Burned vs. Consumed")
            print("2. Category Distribution")
            visChoice = input("Choose what to visualize 1 or 2: ")
            
            if visChoice == "1":
                visCalTrend(user_data["entries"])
            elif visChoice == "2":
                visCatDist(user_data["entries"])
            else:
                print("Invalid visualization option.")
            
        elif choice == "6":
             edit_user_profile(user_data)    
                 
        elif choice == "7":
            save_user_data(username, user_data)
            print("Goodbye!")
            break
        
        
        else:
            print("Invalid choice. Try again.")
        
        


if __name__ == "__main__":
    main()