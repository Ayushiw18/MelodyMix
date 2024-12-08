import java.io.*;
import java.util.*;

public class musicCode {

    static class Node {
        String song;
        Node next;
        Node prev;

        public Node(String song) {
            this.song = song;
            this.next = null;
            this.prev = null;
        }
    }

    static Node top, temp, top1;
    static List<String> playlist = new ArrayList<>();

    // To write a song to a file
    public static void toFile(String song) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("playlist.txt", true))) {
            writer.write(song);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file.");
        }
    }

    // Add a new song node
    public static void addNode(Node first) {
        Scanner sc = new Scanner(System.in);
        while (first.next != null) {
            first = first.next;
        }
        first.next = new Node("");
        first.next.prev = first;
        first = first.next;

        System.out.print("\nEnter Song name: ");
        String song = sc.nextLine();
        first.song = song;
        toFile(song);
    }

    // Delete song from file
    public static void deleteFile(String song) {
        File inputFile = new File("playlist.txt");
        File tempFile = new File("temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String line;
            boolean songFound = false;
            while ((line = reader.readLine()) != null) {
                if (!line.equals(song)) {
                    writer.write(line);
                    writer.newLine();
                } else {
                    songFound = true;
                }
            }
            if (songFound) {
                System.out.println("Song has been deleted.");
            } else {
                System.out.println("There is no song with the name you entered.");
            }
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            System.out.println("Error during file operation.");
        }
    }

    // Print the playlist
    public static void printList(Node first) {
        System.out.println("\nPlaylist:");
        while (first != null) {
            System.out.println(first.song);
            first = first.next;
        }
    }

    // Count total songs in the playlist
    public static void countNodes(Node first) {
        int count = 0;
        while (first != null) {
            first = first.next;
            count++;
        }
        System.out.println("\nTotal songs: " + count);
    }

    // Search for a song
    public static void search1(Node first) {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter song to be searched: ");
        String song = sc.nextLine();

        while (first != null) {
            if (first.song.equals(song)) {
                System.out.println("#Song Found");
                return;
            }
            first = first.next;
        }
        System.out.println("#Song Not Found");
    }

    // Add a song to the recently played list
    public static void push(String song) {
        if (top == null) {
            top = new Node(song);
        } else {
            temp = new Node(song);
            temp.next = top;
            top.prev = temp;
            top = temp;
        }
    }

    // Display recently played songs
    public static void display() {
        top1 = top;
        if (top1 == null) {
            System.out.println("\n=> NO recently played tracks.");
            return;
        }
        System.out.println("\n#Recently played tracks:");
        while (top1 != null) {
            System.out.println(top1.song);
            top1 = top1.next;
        }
    }

    // Play a song from the playlist
    public static void play(Node first) {
        Scanner sc = new Scanner(System.in);
        printList(first);

        System.out.print("\nChoose song you wish to play: ");
        String song = sc.nextLine();

        while (first != null) {
            if (first.song.equals(song)) {
                System.out.println("=> Now Playing: " + song);
                push(song);
                return;
            }
            first = first.next;
        }
        System.out.println("#Song Not Found");
    }

    // Show last played song
    public static void topElement() {
        if (top == null) {
            System.out.println("\n=> No last played tracks.");
            return;
        }
        System.out.println("=> Last Played Song: " + top.song);
    }

    // Sort the playlist
    public static void sort(Node first) {
        if (first == null) {
            return;
        }

        List<Node> nodes = new ArrayList<>();
        Node current = first;
        while (current != null) {
            nodes.add(current);
            current = current.next;
        }

        nodes.sort(Comparator.comparing(n -> n.song));

        Node newHead = nodes.get(0);
        Node prevNode = null;
        for (Node node : nodes) {
            if (prevNode != null) {
                prevNode.next = node;
                node.prev = prevNode;
            }
            prevNode = node;
        }
        prevNode.next = null;
    }

    // Add songs from the file to the playlist
    public static void addPlaylist(Node start) {
        try (BufferedReader reader = new BufferedReader(new FileReader("playlist.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                addNodeFile(start, line);
            }
            System.out.println("Playlist Added");
        } catch (IOException e) {
            System.out.println("Error reading from file.");
        }
    }

    // Helper method to add songs from file
    public static void addNodeFile(Node first, String song) {
        while (first.next != null) {
            first = first.next;
        }
        first.next = new Node(song);
        first.next.prev = first;
    }

    // Delete song by search
    public static void deleteSongBySearch(Node start) {
        Scanner sc = new Scanner(System.in);
        printList(start);

        System.out.print("\nChoose song you wish to delete: ");
        String song = sc.nextLine();

        while (start != null) {
            if (start.song.equals(song)) {
                System.out.println("#Song Found");
                deleteFile(start.song);
                if (start.prev != null) start.prev.next = start.next;
                if (start.next != null) start.next.prev = start.prev;
                return;
            }
            start = start.next;
        }
        System.out.println("#Song Not Found");
    }

    // Delete song by position
    public static void deleteSongByPosition(Node start) {
        Scanner sc = new Scanner(System.in);
        printList(start);

        System.out.print("\nEnter the position of the song to delete: ");
        int pos = sc.nextInt();
        Node current = start;
        int i = 1;
        while (current != null && i < pos) {
            current = current.next;
            i++;
        }
        if (current != null) {
            deleteFile(current.song);
            if (current.prev != null) current.prev.next = current.next;
            if (current.next != null) current.next.prev = current.prev;
            System.out.println("Song deleted from position " + pos);
        } else {
            System.out.println("No song found at position " + pos);
        }
    }

    // Delete menu for selecting deletion method
    public static void deleteMenu(Node start) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Which type of delete do you want?");
        System.out.println("1. By Search");
        System.out.println("2. By Position");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                deleteSongBySearch(start);
                break;
            case 2:
                deleteSongByPosition(start);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    // Main method placed inside the musicCode class
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Node start = new Node("");
        System.out.println("**WELCOME**");
        System.out.print("\nEnter your playlist name: ");
        start.song = sc.nextLine();
        start.next = null;

        do {
            System.out.println("\n1. Add New Song");
            System.out.println("2. Delete Song");
            System.out.println("3. Display Playlist");
            System.out.println("4. Total Songs");
            System.out.println("5. Search Song");
            System.out.println("6. Play Song");
            System.out.println("7. Recently Played List");
            System.out.println("8. Last Played");
            System.out.println("9. Sort Playlist");
            System.out.println("10. Add From File");
            System.out.println("11. Exit");
            System.out.print("\nEnter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addNode(start);
                    break;
                case 2:
                    deleteMenu(start);
                    break;
                case 3:
                    printList(start);
                    break;
                case 4:
                    countNodes(start);
                    break;
                case 5:
                    search1(start);
                    break;
                case 6:
                    play(start);
                    break;
                case 7:
                    display();
                    break;
                case 8:
                    topElement();
                    break;
                case 9:
                    sort(start);
                    printList(start);
                    break;
                case 10:
                    addPlaylist(start);
                    break;
                case 11:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        } while (true);
    }
}
