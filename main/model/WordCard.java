package model;

import java.util.Objects;

public class WordCard {
    // private 변수 word, meaning, example 선언
    private String word;
    private String meaning;
    private String example;

    //생성자 오버로딩
    public WordCard(String word) {
        this.word = word;
    }
    public WordCard(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }
    public WordCard(String word, String meaning, String example) {
        this.word = word;
        this.meaning = meaning;
        this.example = example;
    }

    //set 메서드

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
    public void setExample(String example) {
        this.example = example;
    }

    //카드 데이터 출력 메서드
    public void printCard() {
        System.out.println("단어: " + word);
        System.out.println("뜻: " + meaning);
        System.out.println("예문: " + example);
        System.out.println();
    }

    //get 메서드
    public String getWord() {
        return word;
    }
    public String getMeaning() {
        return meaning;
    }
    public String getExample() {
        return example;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WordCard wordCard = (WordCard) o;
        return Objects.equals(word, wordCard.word);
    }
}
