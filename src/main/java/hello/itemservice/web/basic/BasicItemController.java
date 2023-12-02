package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    // 1. 기본
    //@PostMapping("/add")
    public String addItemV1(@RequestParam String itemName,
                       @RequestParam int price,
                       @RequestParam Integer quantity,
                       Model model) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);
        model.addAttribute("item", item);
        return "basic/item";
    }

    // 2. @ModelAttribute 사용하여 객체까지 생성
    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item")Item item,Model model) {
        itemRepository.save(item);
        model.addAttribute("item", item);
        return "basic/item";
    }

    // 3. addAttribute 생략
    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute("item")Item item){
        itemRepository.save(item);
        return "basic/item";
    }

    // 4. name 지정 안하면 클래스 이름을 소문자로 하여 인식 : Item -> item
    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item){
        itemRepository.save(item);
        return "basic/item";
    }

    // 5. @ModelAttribute 생략
    //@PostMapping("/add")
    public String addItemV5(Item item){
        itemRepository.save(item);
        return "basic/item";
    }

    // 6. Redirect 사용
    //@PostMapping("/add")
    public String addItemV6(Item item){
        itemRepository.save(item);
        return "redirect:/basic/items/" + item.getId();
    }

    // 7. Redirect Attributes - 확인메시지
    @PostMapping("/add")
    public String addItemV7(Item item, RedirectAttributes redirectAttributes){
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String editItem(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }







    /**
     * 기본 데이터
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
