package lk.chamod.mshop.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    private String orderId;

    private String userId;

    private double totalAmount;

    private String status;

    private  long orderDate;

    private List<OrderItem> orderItems;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class OrderItem{
        private String productId;

        private double unitPrice;
        private int quantity;
        private List<CartItem.Attribute> attributes;
        @Data
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Attribute{
            private String name;
            private String value;

        }


    }
}
