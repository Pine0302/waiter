package geektime.springbucks.waiter.controller;


import geektime.springbucks.waiter.controller.request.NewOrderRequest;
import geektime.springbucks.waiter.model.Coffee;
import geektime.springbucks.waiter.model.CoffeeOrder;
import geektime.springbucks.waiter.service.CoffeeOrderService;
import geektime.springbucks.waiter.service.CoffeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/order")
@Slf4j
public class CoffeeOrderController {

    @Autowired
    private CoffeeOrderService coffeeOrderService;

    @Autowired
    private CoffeeService coffeeService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrder create(@RequestBody NewOrderRequest newOrder )
    {
        log.info("request new order {}",newOrder);
        Coffee[] coffeeList = coffeeService.getCoffeeByName(newOrder.getItems()).toArray(new Coffee[] {});
        return coffeeOrderService.createOrder(newOrder.getCustomer(),coffeeList);
    }


}
