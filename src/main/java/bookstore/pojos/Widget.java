package bookstore.pojos;

import java.util.Objects;
import java.util.Scanner;

public class Widget extends Product {
    private String widgetName;
    private double price;

    public Widget() {
        super();
        this.widgetName = "Default Widget";
    }

    public Widget(String widgetName, double price) {
        super(); // Generates the UUID productId
        this.widgetName = widgetName;
        this.price = price;
    }

    public String getWidgetName() { return widgetName; }
    public void setWidgetName(String widgetName) { this.widgetName = widgetName; }

    @Override
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @Override
    public void initialize(Scanner input) {
        System.out.println("Enter Widget Name:");
        this.widgetName = getInput(input, "New Widget");
        System.out.println("Enter Widget Price:");
        this.price = getInput(input, 0.0);
    }

    @Override
    public void edit(Scanner input) {
        System.out.println("Edit Widget Name [" + widgetName + "]:");
        this.widgetName = getInput(input, this.widgetName);
        System.out.println("Edit Price [" + price + "]:");
        this.price = getInput(input, this.price);
    }

    @Override
    public void sellItem() {
        System.out.println("Selling Widget: " + widgetName);
    }

    @Override
    public String toString() {
        return "Widget{name='" + widgetName + "', price=" + price + "} " + super.toString();
    }
}

