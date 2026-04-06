package org.example.computer;



import java.util.Calendar;

public interface PricingStrategy {
    double calculatePrice(double basePrice, Show show);
}

class DynamicPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(double basePrice, Show show) {
        double price = basePrice;

        Calendar now = Calendar.getInstance();
        int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);

        // Weekend surcharge
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            price += 50;
        }

        // Peak hour surcharge
        String showTime = show.getShowTime();
        if (showTime.contains("7:00 PM") || showTime.contains("9:00 PM")) {
            price += 100;
        }

        return price;
    }
}