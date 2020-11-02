package geektime.springbucks.waiter.controller;

import geektime.springbucks.waiter.controller.request.NewCoffeeRequest;
import geektime.springbucks.waiter.model.Coffee;
import geektime.springbucks.waiter.service.CoffeeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

@Controller
@RequestMapping("/coffee")
@Slf4j
public class CoffeeController {
    @Autowired
    private CoffeeService coffeeService;


    @GetMapping(path = "/", params = "!name")
    @ResponseBody
    public List<Coffee> getAll() {
        List<Coffee> coffees = coffeeService.getAllCoffee();

        //lambda 表达式
        coffees.forEach(coffee -> log.info("foreach:{}",coffee));
        //等效匿名内部类
        coffees.forEach(new Consumer<Coffee>() {
            @Override
            public void accept(Coffee coffee) {
                log.info("foreach-anony:{}",coffee);
            }
        });

        Iterator<Coffee> coffeeIterator = coffees.iterator();
        //lambda 表达式
        coffeeIterator.forEachRemaining(s->log.info("foreachRemaining:{}",s));
        //等效匿名内部类
        Iterator<Coffee> coffeeIteratorAnony = coffees.iterator();
        coffeeIteratorAnony.forEachRemaining(new Consumer<Coffee>(){
            @Override
            public void accept(Coffee coffee) {
                log.info("foreachRemaining-anony:{}",coffee);
            }
        });

        return coffeeService.getAllCoffee();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Coffee getById(@PathVariable Long id) {
        Coffee coffee = coffeeService.getCoffee(id);
        log.info("Coffee {}:", coffee);
        return coffee;
    }

    @GetMapping(path = "/", params = "name")
    @ResponseBody
    public Coffee getByName(@RequestParam String name) {
        Coffee coffee = coffeeService.getCoffee(name);
        return coffee;
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addCoffeeWithoutBindingResult(@Valid NewCoffeeRequest newCoffeeRequest) {
        return coffeeService.save(newCoffeeRequest.getName(), newCoffeeRequest.getPrice());
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addJsonCoffeeWithoutBindingResult(@Valid @RequestBody NewCoffeeRequest newCoffee) {
        return coffeeService.save(newCoffee.getName(), newCoffee.getPrice());
    }

    @PostMapping(path = "/bind/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Coffee addCoffeeWithBindingResult(@Valid NewCoffeeRequest newCoffeeRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 这里先简单处理一下，后续讲到异常处理时会改
            log.warn("Binding Errors: {}", bindingResult);
            return null;
        }
        return coffeeService.save(newCoffeeRequest.getName(), newCoffeeRequest.getPrice());
    }

    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public List<Coffee> batchAddCoffee(@RequestParam("file") MultipartFile file) throws IOException {
        //从文件中读取数据
        List<Coffee> coffees = new ArrayList<Coffee>();
        if (!file.isEmpty()) {
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String str;
            while ((str = reader.readLine()) != null) {
                String[] arr = StringUtils.split(str, " ");
                if (arr != null && arr.length == 2) {
                    //将数据插入数据库
                    coffees.add(coffeeService.save(arr[0], Money.of(CurrencyUnit.of("CNY"), NumberUtils.createBigDecimal(arr[1]))));
                }
            }
        }
        return coffees;
    }
}
