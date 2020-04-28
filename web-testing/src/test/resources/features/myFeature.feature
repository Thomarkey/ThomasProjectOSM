Feature: This is my feature

  Scenario Outline: Go to google - search for <search>
    Given I go to https://www.google.be
    When  I search for <search>
    Then  The first result is <result>
    And   I searched for <search>
    And   I found <result>

    Examples:
      | search   | result                                                 |
      | refleqt  | REFLEQT \| Refleqt                                     |
      | cloudway | Driving digital agility in the cloud \| Cloudway       |
      | xplore   | Xplore Group \| Home \| Connect, create and make sense |
      | xti      | XTi: Welkom                                            |

  Scenario: highlight demo
    Given I go to http://the-internet.herokuapp.com/large
    When I highlight the siblings title
    Then I go to http://www.google.be
