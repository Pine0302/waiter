package geektime.springbucks.waiter.controller.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.joda.money.Money;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.List;
@Getter
@Setter
@ToString
public class NewCoffeeRequest {
        @NotEmpty
        private String name;
        @NotNull
        private Money Price;
}
