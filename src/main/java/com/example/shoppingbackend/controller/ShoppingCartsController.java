package com.example.shoppingbackend.controller;

import com.example.shoppingbackend.entity.Cart;
import com.example.shoppingbackend.entity.Item;
import com.example.shoppingbackend.repository.CartRepository;
import com.example.shoppingbackend.repository.ItemRepository;
import com.example.shoppingbackend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ShoppingCartsController {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    JwtService jwtService;

    @GetMapping("/api/cart/items") /* 카트 안에 있는 아이템 불러오기 */
    public ResponseEntity getCartItems(@CookieValue(value = "token", required = false) String token) {

        if (!jwtService.isValid(token)) { /* token 값이 유효하지 않음 */
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        List<Cart> carts = cartRepository.findByMemberId(memberId); /* Cart 테이블에 Item ID 값만 담겨져 있으므로 실질적인 Item은 확인할 수 없다. */
        List<Integer> itemIds = carts.stream().map(Cart::getItemId).toList(); /* Cart 테이블 중에서 Item ID 값만 가져온다. */
        List<Item> items = itemRepository.findByIdIn(itemIds); /* Item ID를 통해 item 리스트를 얻는다. */

        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @DeleteMapping("/api/cart/items/{itemId}") /* 카트 안에 있는 아이템 삭제하기 */
    public ResponseEntity removeCartItems(@PathVariable("itemId") int itemId, @CookieValue(value = "token", required = false) String token) {

        if (!jwtService.isValid(token)) { /* token 값이 유효하지 않음 */
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);

        cartRepository.delete(cart);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/api/cart/items/{itemId}") /* 카트에 새로운 아이템 담기 */
    public ResponseEntity pushCartItem(@PathVariable("itemId") int itemId, @CookieValue(value = "token", required = false) String token) {

        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);

        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setMemberId(memberId);
            newCart.setItemId(itemId);
            cartRepository.save(newCart);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
