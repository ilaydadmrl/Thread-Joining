package ceng.estu.edu.utils;

import java.util.HashSet;
import java.util.Random;

public class Node extends Thread {
    public HashSet<Node> prerequisites = new HashSet<>();
    public String name;
    private final Random random = new Random();
    public Node(String name) {
        this.name = name;
    }
    public void perform() {
        this.start();
    }

    @Override
    public boolean equals(Object object) {
        return (object instanceof Node && ((Node) object).name.equals(this.name));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public void run() {
        try {
            waitForPrerequisites();
            System.out.printf("Node%s is being started%n", name);
            Thread.sleep(random.nextInt(2000));
            System.out.printf("Node%s is completed%n", name);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void waitForPrerequisites() throws InterruptedException {

        if (prerequisites.size() > 0) {
            StringBuilder waitingFor = new StringBuilder(String.format("Node%s is waiting for ", name));
            int count = 0;
            for (Node node : prerequisites) {
                count++;
                waitingFor.append(node.name).append(count == prerequisites.size() ? "" : ",");
            }

            System.out.println(waitingFor);


            Node[] threads = prerequisites.toArray(new Node[0]);

            int index = 0;
            while (count > 0) {
                Node node = threads[index];
                if (node.isAlive()) {
                    Thread.sleep(random.nextInt(2000));
                } else {
                    count--;
                    index++;
                }
            }
        }
    }
}
