package service;

import model.WordCard;
import repository.Repository;

import java.util.ArrayList;
import java.util.Scanner;

public class Service {

    private final Repository repository = new Repository();

    public void addCard(Scanner scanner) {
        System.out.print("단어: ");
        String word = scanner.nextLine();

        if (word.isEmpty()) {
            System.out.println("단어는 비워둘 수 없습니다.");
            return;
        }

        System.out.print("뜻: ");
        String meaning = scanner.nextLine();

        System.out.print("예문: ");
        String example = scanner.nextLine();

        repository.add(new WordCard(word, meaning, example));
        System.out.println(word + " 카드가 추가되었습니다.");
    }

    public void printList() {
        ArrayList<WordCard> cards = repository.findAll();

        if (cards.isEmpty()) {
            System.out.println("저장된 카드가 없습니다.");
            return;
        }

        for (WordCard card : cards) {
            System.out.println("===========");
            card.printCard();
        }
    }

    public void searchCard(Scanner scanner) {
        System.out.print("검색할 단어: ");
        String searchWord = scanner.nextLine();

        WordCard card = repository.findByWord(searchWord);

        if (card != null) {
            System.out.println("검색 결과: ");
            card.printCard();
        } else {
            System.out.println("해당 단어를 찾을 수 없습니다.");
        }
    }

    public void quiz(Scanner scanner) {
        ArrayList<WordCard> cards = repository.findAll();

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
            }
        }

        System.out.println("점수 총합: " + score);
    }

    public void deleteCard(Scanner sc) {
        System.out.print("삭제할 단어를 입력하세요: ");
        String target = sc.nextLine();

        boolean deleted = repository.deleteByWord(target);

        if (deleted) {
            System.out.println(target + " 카드를 삭제했습니다.");
        } else {
            System.out.println("해당 카드가 존재하지 않습니다.");
        }
    }

    public void updateCard(Scanner scanner) {
        System.out.print("수정할 단어를 입력하세요: ");
        String word = scanner.nextLine();

        WordCard card = repository.findByWord(word);

        if (card != null) {
            while (true) {

                System.out.println("===== 메뉴 =====");
                System.out.println("1. 뜻 수정");
                System.out.println("2. 예문 수정");
                System.out.println("3. 수정 종료");
                System.out.println("===============");
                System.out.print("메뉴를 선택하세요: ");

                int select;
                try {
                    select = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("숫자를 입력해 주세요.");
                    continue;
                }

                if (select == 1) {
                    System.out.print("새 뜻을 입력하세요: ");
                    String newMean = scanner.nextLine();
                    repository.updateMeaning(card, newMean);
                    System.out.println("수정을 완료했습니다.");
                } else if (select == 2) {
                    System.out.print("새 예문을 입력하세요: ");
                    String newExample = scanner.nextLine();
                    repository.updateExample(card,newExample);
                    System.out.println("수정을 완료했습니다.");
                } else if (select == 3) {
                    System.out.println("수정 모드를 종료합니다.");
                    break;
                } else {
                    System.out.println("잘못된 입력입니다.");
                }
            }
        } else {
            System.out.println("해당 단어를 찾을 수 없습니다.");
        }
    }
}
