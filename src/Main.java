import java.util.*;

class ProjectMain {

    // Queue (front = next to serve)
    static ArrayDeque<String> line = new ArrayDeque<>();

    // Arrival time for each person
    static HashMap<String, Integer> arrivalTime = new HashMap<>();

    // Logical time
    static int currentTime = 0;

    // Statistics
    static long totalWait = 0;
    static int servedCount = 0;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        printHelp();

        while (true) {

            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Invalid command.");
                continue;
            }

            String[] parts = input.split("\\s+");
            String command = parts[0].toUpperCase();

            switch (command) {

                case "HELP":
                    printHelp();
                    break;

                case "ARRIVE":
                    if (parts.length < 2) {
                        System.out.println("Usage: ARRIVE <name>");
                    } else {
                        arrive(parts[1]);
                    }
                    break;

                case "VIP_ARRIVE":
                    if (parts.length < 2) {
                        System.out.println("Usage: VIP_ARRIVE <name>");
                    } else {
                        vipArrive(parts[1]);
                    }
                    break;

                case "SERVE":
                    serve();
                    break;

                case "LEAVE":
                    if (parts.length < 2) {
                        System.out.println("Usage: LEAVE <name>");
                    } else {
                        leave(parts[1]);
                    }
                    break;

                case "PEEK":
                    peek();
                    break;

                case "SIZE":
                    size();
                    break;

                case "PRINT":
                    printLine();
                    break;

                case "TICK":
                    if (parts.length < 2) {
                        System.out.println("Usage: TICK <minutes>");
                    } else {
                        tick(parts[1]);
                    }
                    break;

                case "STATS":
                    stats();
                    break;

                case "EXIT":
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Unknown command. Type HELP.");
            }
        }
    }

    // Print help menu
    static void printHelp() {

        System.out.println("Cafeteria Line Manager — Commands:");
        System.out.println("HELP");
        System.out.println("ARRIVE <name>");
        System.out.println("VIP_ARRIVE <name>");
        System.out.println("SERVE");
        System.out.println("LEAVE <name>");
        System.out.println("PEEK");
        System.out.println("SIZE");
        System.out.println("PRINT");
        System.out.println("TICK <minutes>");
        System.out.println("STATS");
        System.out.println("EXIT");
    }

    // Validate name
    static boolean validName(String name) {
        return name != null && !name.isEmpty() && !name.contains(" ");
    }

    // Check duplicate
    static boolean exists(String name) {
        return arrivalTime.containsKey(name);
    }

    // ARRIVE command
    static void arrive(String name) {

        if (!validName(name)) {
            System.out.println("Invalid name.");
            return;
        }

        if (exists(name)) {
            System.out.println("Name already in system");
            return;
        }

        line.addLast(name);
        arrivalTime.put(name, currentTime);

        System.out.println(name + " arrived at time " + currentTime +
                ". Line size = " + line.size());
    }

    // VIP_ARRIVE command
    static void vipArrive(String name) {
        if (!validName(name)) {
            System.out.println("Invalid name.");
            return;
        }

        if (exists(name)) {
            System.out.println("Name already in system");
            return;
        }

        line.addFirst(name);
        arrivalTime.put(name, currentTime);

        System.out.println("VIP " + name + " arrived at time " +
                currentTime + " (front). Line size = " + line.size());
    }

    // SERVE command
    static void serve() {

        if (line.isEmpty()) {
            System.out.println("No one to serve.");
            return;
        }

        String person = line.removeFirst();
        int arrival = arrivalTime.get(person);

        int wait = currentTime - arrival;

        totalWait += wait;
        servedCount++;

        arrivalTime.remove(person);

        System.out.println("Served: " + person +
                " (waited " + wait + " min).");
    }

    // LEAVE command
    static void leave(String name) {

        if (!line.removeFirstOccurrence(name)) {
            System.out.println("Not found");
            return;
        }

        arrivalTime.remove(name);

        System.out.println(name +
                " left the line voluntarily. Line size = " +
                line.size());
    }

    // PEEK command
    static void peek() {

        if (line.isEmpty()) {
            System.out.println("Line is empty.");
            return;
        }

        System.out.println("Next: " + line.peekFirst());
    }

    // SIZE command
    static void size() {
        System.out.println("Size: " + line.size());
    }

    // PRINT command
    static void printLine() {
        System.out.println("Line (front -> back): " + line);
    }

    // TICK command
    static void tick(String value) {

        try {

            int minutes = Integer.parseInt(value);

            if (minutes < 0) {
                System.out.println("Minutes must be >= 0.");
                return;
            }

            currentTime += minutes;

            System.out.println("Time advanced by " + minutes +
                    " minutes. Current time = " + currentTime);

        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
        }
    }

    // STATS command
    static void stats() {

        double avg = 0.0;

        if (servedCount > 0) {
            avg = (double) totalWait / servedCount;
        }

        System.out.printf(
                "Served count = %d, Avg wait = %.2f min.%n",
                servedCount,
                avg
        );
    }
}