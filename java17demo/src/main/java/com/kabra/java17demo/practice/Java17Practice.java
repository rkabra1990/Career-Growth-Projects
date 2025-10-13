package com.kabra.java17demo.practice;

import java.time.Month;

public class Java17Practice {
    // Java17FeaturesPractice.java
// Works on Java 17 without enabling preview features

    // =====================
    // 1️⃣ RECORDS
    // =====================

    public record Employee(String name, int id, double salary) {
    }

    public record Point(double x, double y) {
        public double distance(Point other) {
            double dx = x - other.x;
            double dy = y - other.y;
            return Math.sqrt(dx * dx + dy * dy);
        }
    }

    public record Product(String name, double price, int quantity) {
        // Compact constructor with validation
        public Product {
            if (price < 0 || quantity < 0) {
                throw new IllegalArgumentException("Price and quantity must be non-negative");
            }
        }

        public double totalCost() {
            return price * quantity;
        }
    }

    // =====================
    // 2️⃣ SEALED CLASSES
    // =====================

    public sealed interface Vehicle permits Car, Bike, Truck {
    }

    public final static class Car implements Vehicle {
        public final String model;

        public Car(String model) {
            this.model = model;
        }

        @Override
        public String toString() {
            return "Car: " + model;
        }
    }

    public final static class Bike implements Vehicle {
        public final String brand;

        public Bike(String brand) {
            this.brand = brand;
        }

        @Override
        public String toString() {
            return "Bike: " + brand;
        }
    }

    public final static class Truck implements Vehicle {
        public final int capacity;

        public Truck(int capacity) {
            this.capacity = capacity;
        }

        @Override
        public String toString() {
            return "Truck with capacity: " + capacity + " tons";
        }
    }

    // =====================
    // 3️⃣ PATTERN MATCHING
    // =====================

    public static void printLength(Object obj) {
        if (obj instanceof String str) {
            System.out.println("Length of string: " + str.length());
        } else {
            System.out.println("Not a string");
        }
    }

    public static void processNumber(Object obj) {
        if (obj instanceof Integer i) {
            System.out.println(i + (i % 2 == 0 ? " is even" : " is odd"));
        } else if (obj instanceof Double d) {
            System.out.println(d + " is a decimal number");
        } else {
            System.out.println("Not a number");
        }
    }

    // =====================
    // 4️⃣ SWITCH ENHANCEMENTS
    // =====================

    public static String seasonFromMonth(Month month) {
        return switch (month) {
            case DECEMBER, JANUARY, FEBRUARY -> "Winter";
            case MARCH, APRIL, MAY -> "Spring";
            case JUNE, JULY, AUGUST -> "Summer";
            case SEPTEMBER, OCTOBER, NOVEMBER -> "Autumn";
        };
    }

    public static double calculateBonus(String level) {
        return switch (level) {
            case "JUNIOR" -> 5000;
            case "MID" -> 10000;
            case "SENIOR" -> 20000;
            default -> 0;
        };
    }

    // =====================
    // 5️⃣ COMBINED FEATURE DEMO
    // =====================

    // Sealed hierarchy with records
    sealed interface Shape permits Circle, Rectangle, Triangle {
    }

    record Circle(double radius) implements Shape {
    }

    record Rectangle(double width, double height) implements Shape {
    }

    record Triangle(double base, double height) implements Shape {
    }

    public static double areaOf(Shape shape) {
        if (shape instanceof Circle c) {
            return Math.PI * c.radius() * c.radius();
        } else if (shape instanceof Rectangle r) {
            return r.width() * r.height();
        } else if (shape instanceof Triangle t) {
            return 0.5 * t.base() * t.height();
        } else {
            throw new IllegalArgumentException("Unknown shape");
        }
    }

    // =====================
    // MAIN METHOD
    // =====================

    public static void main(String[] args) {
        System.out.println("=== RECORDS ===");
        Employee emp = new Employee("Ronak", 101, 85000);
        System.out.println(emp);

        Point p1 = new Point(3, 4);
        Point p2 = new Point(6, 8);
        System.out.println("Distance between points: " + p1.distance(p2));

        Product prod = new Product("Laptop", 50000, 2);
        System.out.println("Total cost: " + prod.totalCost());

        System.out.println("\n=== SEALED CLASSES ===");
        Vehicle v1 = new Car("Tesla");
        Vehicle v2 = new Bike("Yamaha");
        Vehicle v3 = new Truck(10);
        System.out.println(v1);
        System.out.println(v2);
        System.out.println(v3);

        System.out.println("\n=== PATTERN MATCHING ===");
        printLength("Hello Java 17");
        printLength(1234);
        processNumber(42);
        processNumber(3.14);
        processNumber("Not a number");

        System.out.println("\n=== SWITCH ENHANCEMENTS ===");
        System.out.println("Season in July: " + seasonFromMonth(Month.JULY));
        System.out.println("Bonus for MID level: " + calculateBonus("MID"));

        System.out.println("\n=== COMBINED FEATURE DEMO ===");
        Shape s1 = new Circle(10);
        Shape s2 = new Rectangle(5, 7);
        Shape s3 = new Triangle(6, 4);

        System.out.println("Area of Circle: " + areaOf(s1));
        System.out.println("Area of Rectangle: " + areaOf(s2));
        System.out.println("Area of Triangle: " + areaOf(s3));
    }
}

