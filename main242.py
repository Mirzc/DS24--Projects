import os
import json
import csv
import xml.etree.ElementTree as ET

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

def main():
    print("=== Fitness & Nutrition Tracker ===")
    username = input("Enter your username: ")
    user_data = load_user_data(username)

    while True:
        print("\nChoose an option:")
        print("1. Add entry manually")
        print("2. Import entries from CSV")
        print("3. Import entries from XML")
        print("4. View all entries")
        print("5. Save and Exit")

        choice = input("Your choice: ")

        if choice == "1":
            entry = get_manual_entry()
            add_entry_to_user(entry, user_data)

        elif choice == "2":
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
            print("\n--- All Entries ---")
            for i, entry in enumerate(user_data["entries"], 1):
                print(f"{i}. {entry}")

        elif choice == "5":
            save_user_data(username, user_data)
            print("Goodbye!")
            break

        else:
            print("Invalid choice. Try again.")


if __name__ == "__main__":
    main()