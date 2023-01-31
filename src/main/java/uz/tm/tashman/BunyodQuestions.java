package uz.tm.tashman;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BunyodQuestions {

    public static Map<Character, Integer> extractLetters(String note) {
        Map<Character, Integer> lettersOfNote = new HashMap<>();

        for (int i = 0; i < note.length(); i++) {
            if (lettersOfNote.containsKey(note.charAt(i))) {
                lettersOfNote.put(note.charAt(i), lettersOfNote.get(note.charAt(i)) + 1);
            } else {
                lettersOfNote.put(note.charAt(i), 1);
            }
        }

        return lettersOfNote;
    }

    public static void question1() {
        String[] words = new String[]{"baby", "referee", "cat", "dada", "dog", "bird", "ax", "baz"};
        String note = "ctay";

        Map<Character, Integer> lettersOfNote = extractLetters(note);

        for (String word : words) {
            if (word.length() <= note.length()) {
                Map<Character, Integer> lettersOfWord = extractLetters(word);
                boolean alwaysTrue = true;
                for (Map.Entry<Character, Integer> entry : lettersOfWord.entrySet()) {
                    Character letter = entry.getKey();
                    Integer count = entry.getValue();
                    if (lettersOfNote.containsKey(letter)) {
                        if (count > lettersOfNote.get(letter)) {
                            alwaysTrue = false;
                            break;
                        }
                    } else {
                        alwaysTrue = false;
                        break;
                    }
                }
                if (alwaysTrue) {
                    System.out.println(word);
                }
            }
        }
    }

    public static void question3() {

//        List<Users> rUsers = userRepository.getUsers();
//        List<Payments> rPayments = paymentRepository.getPayments();
//        List<Tickets> rTickets = ticketsRepository.getTickets();

//        String queryByEmail = "select u.username, p.id from users u join payments p on u.email=p.email";
//        String queryByAmount = "with userAndTicket as (select u.username, (u.quantity*t.price) as amount from users u join tickets t on u.purchase=t.ticket)" +
//                "select uat.username, p.id from userAndTicket aut join payment p on uat.amount=p.amount";
//        List<ResponseDTO> resp = queryByEmail.execute();

        List<Users> users = List.of(
                new Users("Matthew R.", "mr@test.com", "OneEco", 5),
                new Users("Linda F.", "l.f@mail.com", "Top", 10)
        );
        List<Payments> payments = List.of(
                new Payments(10, "mr@test.com", 5),
                new Payments(6, "email not found", 110)
        );
        List<Tickets> tickets = List.of(
                new Tickets("OneEco", 1),
                new Tickets("Top", 11)
        );


        for (Users user : users) {
            ResponseDTO response = new ResponseDTO();
            response.username = user.username;

            for (Payments payment : payments) {
                if (payment.email.equals(user.email)) {
                    response.paymentId = payment.id;
                }
            }

            if (response.paymentId == null) {
                for (Tickets ticket : tickets) {
                    if (user.purchase.equals(ticket.ticket)) {
                        for (Payments payment : payments) {
                            if (user.quantity * ticket.price == payment.amount) {
                                response.paymentId = payment.id;
                            }
                        }
                    }
                }
            }

            System.out.println(response.username + " - " + response.paymentId);
        }


    }

    public static void main(String[] args) {
        question1();

        question3();
    }
}

class Users {
    String username;
    String email;
    String purchase;
    Integer quantity;

    public Users(String username, String email, String purchase, Integer quantity) {
        this.username = username;
        this.email = email;
        this.purchase = purchase;
        this.quantity = quantity;
    }
}

class Tickets {
    String ticket;
    Integer price;

    public Tickets(String ticket, Integer price) {
        this.ticket = ticket;
        this.price = price;
    }
}

class Payments {
    Integer id;
    String email;
    Integer amount;

    public Payments(Integer id, String email, Integer amount) {
        this.id = id;
        this.email = email;
        this.amount = amount;
    }
}

class ResponseDTO {
    String username;
    Integer paymentId;
}