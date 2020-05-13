Feature: Crawling transferlist

  Scenario: list Transferpage to CSV
    Given I go to http://www.onlinesoccermanager.nl
    And I login with my credentials
    And I choose my team
    And I navigate to the transferlist
    When I browse the transferlist
    Then I create a CSV from all the players in the transferlist



  @wip
  Scenario: Decide who to sell
    Given I go to http://www.onlinesoccermanager.nl
    And I login with my credentials
    And I choose my team
    And I navigate to my selectie
    When I browse my team
    Then I sell the right people
