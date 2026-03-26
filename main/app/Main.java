package app;

import service.Service;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Service service = new Service();

        while (true) {

            printMenu();

            int select;

            try {
                select = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해 주세요.");
                continue;
            }

            if (select == 1) {
                service.addCard(scanner);
            } else if (select == 2) {
                service.printList();
            } else if (select == 3) {
                service.searchCard(scanner);
            } else if (select == 4) {
                service.updateCard(scanner);
            } else if (select == 5) {
                service.deleteCard(scanner);
            } else if (select == 6) {
                System.out.println("단어 퀴즈를 시작합니다.");
                service.quiz(scanner);
            } else if (select == 7) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else {
                System.out.println("잘못된 입력입니다.");
            }
        }

        scanner.close();
    }

    private static void printMenu() {
        System.out.println("======= 메뉴 =======");
        System.out.println("1. 카드 추가");
        System.out.println("2. 카드 목록");
        System.out.println("3. 카드 검색");
        System.out.println("4. 카드 수정");
        System.out.println("5. 카드 삭제");
        System.out.println("6. 단어 퀴즈 시작");
        System.out.println("7. 종료");
        System.out.println("===================");
        System.out.print("메뉴를 선택하세요: ");

    }
}

