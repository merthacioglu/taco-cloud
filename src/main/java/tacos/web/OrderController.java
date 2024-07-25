package tacos.web;
import javax.validation.Valid;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import tacos.TacoOrder;
import tacos.User;
import tacos.data.OrderRepository;

import java.util.Optional;

@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

  private OrderRepository orderRepo;

  private OrderProps props;

  public OrderController(OrderRepository orderRepo,
          OrderProps props) {
    this.orderRepo = orderRepo;
    this.props = props;
  }

  @GetMapping("/current")
  public String orderForm(@AuthenticationPrincipal User user,
      @ModelAttribute TacoOrder order) {
    if (order.getDeliveryName() == null) {
      order.setDeliveryName(user.getFullname());
    }
    if (order.getDeliveryStreet() == null) {
      order.setDeliveryStreet(user.getStreet());
    }
    if (order.getDeliveryCity() == null) {
      order.setDeliveryCity(user.getCity());
    }
    if (order.getDeliveryState() == null) {
      order.setDeliveryState(user.getState());
    }
    if (order.getDeliveryZip() == null) {
      order.setDeliveryZip(user.getZip());
    }

    return "orderForm";
  }

  @PostMapping
  public String processOrder(@Valid TacoOrder order, Errors errors,
      SessionStatus sessionStatus,
      @AuthenticationPrincipal User user) {

    if (errors.hasErrors()) {
      return "orderForm";
    }

    order.setUser(user);

    orderRepo.save(order);
    sessionStatus.setComplete();

    return "redirect:/";
  }

  @GetMapping
  public String ordersForUser(
      @AuthenticationPrincipal User user, Model model) {

    Pageable pageable = PageRequest.of(0, props.getPageSize());
    model.addAttribute("orders",
        orderRepo.findByUserOrderByPlacedAtDesc(user, pageable));

    return "orderList";
  }

  @PutMapping(path = "/{orderId}", consumes = "application/json")
  public TacoOrder putOrder(@PathVariable("orderId") Long orderId,
                            @RequestBody TacoOrder order) {
    order.setId(orderId);
    return orderRepo.save(order);
  }

  @PatchMapping(path="/{orderId}", consumes="application/json")
  public ResponseEntity<TacoOrder> patchOrder(@PathVariable("orderId") Long orderId,
                                              @RequestBody TacoOrder patch) {
    Optional<TacoOrder> orderOptional = orderRepo.findById(orderId);
    if (orderOptional.isPresent()) {
      TacoOrder order = orderOptional.get();
      if (patch.getDeliveryName() != null) {
        order.setDeliveryName(patch.getDeliveryName());
      }
      if (patch.getDeliveryStreet() != null) {
        order.setDeliveryStreet(patch.getDeliveryStreet());
      }
      if (patch.getDeliveryCity() != null) {
        order.setDeliveryCity(patch.getDeliveryCity());
      }
      if (patch.getDeliveryState() != null) {
        order.setDeliveryState(patch.getDeliveryState());
      }
      if (patch.getDeliveryZip() != null) {
        order.setDeliveryZip(patch.getDeliveryZip());
      }
      if (patch.getCcNumber() != null) {
        order.setCcNumber(patch.getCcNumber());
      }
      if (patch.getCcExpiration() != null) {
        order.setCcExpiration(patch.getCcExpiration());
      }
      if (patch.getCcCVV() != null) {
        order.setCcCVV(patch.getCcCVV());
      }
      return ResponseEntity.ok(orderRepo.save(order));
    } else {
      return ResponseEntity.notFound().build();
    }


  }


  @DeleteMapping("/{orderId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteOrder(@PathVariable("orderId") Long orderId) {
    try {
      orderRepo.deleteById(orderId);
    } catch (EmptyResultDataAccessException e) {}
  }

  /*
  @GetMapping
  public String ordersForUser(
      @AuthenticationPrincipal User user, Model model) {

    Pageable pageable = PageRequest.of(0, 20);
    model.addAttribute("orders",
        orderRepo.findByUserOrderByPlacedAtDesc(user, pageable));

    return "orderList";
  }
   */

  /*
  @GetMapping
  public String ordersForUser(
      @AuthenticationPrincipal User user, Model model) {

    model.addAttribute("orders",
        orderRepo.findByUserOrderByPlacedAtDesc(user));

    return "orderList";
  }
   */

}
