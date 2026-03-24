package app;

import model.WordCard;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        //동적 배열 생성
        ArrayList<WordCard> cards = new ArrayList<>();

        while (true) {
            System.out.println("==================");
            System.out.println("메뉴를 선택하세요.");
            System.out.println("1. 카드 추가");
            System.out.println("2. 카드 목록");
            System.out.println("3. 카드 검색");
            System.out.println("4. 단어 퀴즈 시작");
            System.out.println("5. 카드 삭제");
            System.out.println("6. 종료");
            System.out.println("==================");
            int select = scanner.nextInt();
            scanner.nextLine();

            if (select == 1) {
                addCard(cards, scanner);
            } else if (select == 2) {
                printList(cards);
            } else if (select == 3) {
                searchCard(cards, scanner);
            } else if (select == 4) {
                System.out.println("단어 퀴즈를 시작합니다.");
                quiz(cards, scanner);
            } else if (select == 5) {
                deleteCard(cards, scanner);
            } else if (select == 6) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else {
                System.out.println("잘못된 입력입니다.");
            }
        }

        scanner.close();
    }

    private static void addCard(ArrayList<WordCard> cards, Scanner scanner) {
        System.out.print("단어: ");
        String word = scanner.nextLine();

        System.out.print("뜻: ");
        String meaning = scanner.nextLine();

        System.out.print("예문: ");
        String example = scanner.nextLine();

        cards.add(new WordCard(word, meaning, example));
        System.out.println(word + " 카드가 추가되었습니다.");
    }

    private static void printList(ArrayList<WordCard> cards) {
        if (cards.isEmpty()) {
            System.out.println("저장된 카드가 없습니다.");
            return;
        }

        for (WordCard card : cards) {
            System.out.println("===========");
            card.printCard();
        }
    }

    private static void searchCard(ArrayList<WordCard> cards, Scanner scanner) {
        System.out.print("검색할 단어: ");
        String searchWord = scanner.nextLine();

        for (WordCard card : cards) {
            if (card.getWord().equals(searchWord)) {
                System.out.println("검색 결과: ");
                card.printCard();
                return;
            }
        }

        System.out.println("해당 단어를 찾을 수 없습니다.");
    }

    private static void quiz(ArrayList<WordCard> cards, Scanner scanner) {
        if (cards.isEmpty()) {
            System.out.println("카드가 존재하지 않습니다.");
            return;
        }

        int score = 0;

        for (WordCard card : cards) {
            String word = card.getWord();

            System.out.println(word);
            System.out.print("뜻을 입력하세요: ");
            String answer = scanner.nextLine();

            if (answer.equals(card.getMeaning())) {
                System.out.println("정답입니다!");
                score++;
            } else {
                System.out.println("오답입니다. 정답은 " + card.getMeaning() + "입니다.");
                exampleQuery(card, scanner);
            }
        }

        System.out.println("점수 총합: " + score);
    }

    private static void exampleQuery (WordCard card, Scanner scanner){
        System.out.print("예문을 입력하시겠습니까? [Y/n]: ");
        String choice = scanner.nextLine();

        if (choice.equals("Y")) {
            System.out.println("예문을 입력하세요.");
            String example = scanner.nextLine();
            card.setExample(example);

            System.out.println("카드의 정보는 다음과 같습니다.");
            card.printCard();
        }
    }

    private static void deleteCard(ArrayList<WordCard> cards, Scanner sc) {
        System.out.print("삭제할 단어를 입력하세요: ");
        String target = sc.nextLine();

        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getWord().equals(target)) {
                cards.remove(i);
                System.out.println(target + " 카드를 삭제했습니다.");
                return;
            }
        }

        System.out.println("해당 카드가 존재하지 않습니다.");
    }
}

