package org.czaplinski.bookshop.order.domain;

import lombok.Value;

@Value
public class  UpdateStatusResult {
    OrderStatus status;
    boolean revoke;

   static UpdateStatusResult ok(OrderStatus newStatus){
       return new UpdateStatusResult(newStatus, false);
   }
   static UpdateStatusResult revoke(OrderStatus newStatus){
       return new UpdateStatusResult(newStatus, true);
   }
}
