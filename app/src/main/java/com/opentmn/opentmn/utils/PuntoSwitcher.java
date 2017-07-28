package com.opentmn.opentmn.utils;

/**
 * Created by egorov on 28.07.2017.
 */
import java.util.HashMap;
import java.util.Map;

public class PuntoSwitcher {

  private static Map<Character, Character> EN_TO_RU_MAP = new HashMap<>();

  static {
    String normalPairs = "аaбbвvгgдdеeёeжzзzиiйyкkлlмmнnоoпpрrсsтtуuфfхhцcчhшhщhъ ыyь эeюyяy";
    addPairsToMap(normalPairs, EN_TO_RU_MAP);
  }

  private static void addPairsToMap(String pairs, Map<Character, Character> map) {
    for (int i = 0; i < pairs.length(); i += 2) {
      map.put(pairs.charAt(i), pairs.charAt(i + 1));
    }
  }

  public static String switchToRu(String phrase) {
    if (phrase == null) {
      return null;
    }

    char[] switchedChars = phrase.toCharArray();
    for (int i = 0; i < switchedChars.length; i++) {
      Character ruChar = EN_TO_RU_MAP.get(switchedChars[i]);
      if (ruChar != null) {
        switchedChars[i] = ruChar;
      }
    }

    return new String(switchedChars);
  }
}
