import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final Random rand = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        int gameOver = 0;
        int populationCount = 100;
        int acresOfFarmland = 1000;
        int harvestCount = 3000;
        int starvedCount = 0;
        int arrivalCount = 5;
        int yearCount = 0;
        int ratFood = 200;
        int storeCount = 2800;
        int acresPlanted;
        int foodReserved;
        int totalStarveCount = 0;
        float plagueProbability = 0.15f;


        while (gameOver == 0) {
            reportPopulation(yearCount, starvedCount, arrivalCount, populationCount);
            reportHarvest(acresOfFarmland, harvestCount, ratFood, storeCount);

            if (yearCount > 0) {
                populationCount = simulatePlague(plagueProbability, populationCount);
            }

            int[] buyData = buyLand(storeCount);
            storeCount -= buyData[0];
            acresOfFarmland += buyData[1];

            int[] sellData = sellLand(acresOfFarmland);
            storeCount += sellData[0] * sellData[1];
            acresOfFarmland -= sellData[1];

            int[] feedData = feedPeople(storeCount);
            storeCount = feedData[0];
            foodReserved = feedData[1];

            int[] plantData = plantCrops(storeCount, acresOfFarmland, populationCount);
            storeCount -= plantData[0];
            acresPlanted = plantData[1];

            int[] harvestData = harvestCrops(acresPlanted, storeCount);
            storeCount += harvestData[0] - harvestData[2];
            harvestCount = harvestData[0];
            ratFood = harvestData[1];

            int[] popData = populationChange(acresOfFarmland, storeCount, populationCount, foodReserved);
            arrivalCount = popData[0];
            starvedCount = popData[1];
            totalStarveCount += popData[1];
            populationCount = popData[2];
            gameOver = popData[3];


            yearCount++;
//            System.out.printf("Population Count: %d \n Farmland Count: %d \n Harvest Count: %d \n Starved Count: %d " +
//                            "\n Arrival Count: %d \n Year Count: %d \n Rat Food: %d \n Store Count: %d \n Plague: %.2f \n Food Reserved: %d \n Acres Planted: %d\n",
//                    populationCount, acresOfFarmland, harvestCount, starvedCount, arrivalCount, yearCount, ratFood, storeCount,
//                    plagueProbability, foodReserved, acresPlanted);

            if (yearCount > 10) {
                gameOver = 2;
            }
        }

        if (gameOver == 2) {
            int starvePercent = (totalStarveCount * 100) / (yearCount * populationCount);
            int acresPerPerson = (acresOfFarmland / populationCount);
            System.out.printf("In your ten year term of office, %d percent of the population starved per year on average, " +
                    "i.e. A total of %d people died!%n", starvePercent, totalStarveCount);
            System.out.printf("You started with 10 acres per person and ended with %d acres per person!%n", acresPerPerson);
            if (starvePercent > 33 || acresPerPerson < 7) {
                gameOver = 1;
            } else if (starvePercent > 10 || acresPerPerson < 9) {
                System.out.println("Your heavy handed performance smacks of Nero and Ivan IV " +
                        ".The people (remaining) find you an unpleasant ruler, and " +
                        "frankly hate your guts!!");
            } else if (starvePercent > 3 || acresPerPerson < 10) {
                int randomNum = rand.nextInt(1, 80);
                System.out.printf("Your performance could have been somewhat better, but" +
                        "really was not too bad at all. %d people" +
                        "would dearly like to see you assassinated but we all have" +
                        "our trivial problems.", (populationCount * randomNum) / 100);
            } else {
                System.out.println("A fantastic performance!! Charlemagne, Disraeli, and" +
                        "Jefferson could not have done better!”");
            }

        }
        if (gameOver == 1) {
            System.out.printf("You starved %d people in one year!! " +
                    "Due to this extreme mismanagement you have not only" +
                    " been impeached and thrown out of office but you have " +
                    "also been declared national fink!!!%n", starvedCount);
        }

    }

    private static void reportPopulation(int yearCount, int starvedCount, int arrivalCount, int populationCount) {
        System.out.printf(
                "Hamurabi! I beg to report to you, in the year %d, a total of %d people starved, and %d arrived resulting " +
                        "in a total population of %d.%n",
                yearCount, starvedCount, arrivalCount, populationCount);
    }

    private static int simulatePlague(float plagueProb, int population) {
        if (calculatePlagueChance(plagueProb)) {
            population = executePlague(population);
        }
        return population;
    }

    private static boolean calculatePlagueChance(float plagueProb) {
        return (Math.random() < plagueProb);
    }

    private static int executePlague(int populationCount) {
        System.out.printf("A horrible plague struck the city! Half of your citizens have perished. " +
                "Theres only %d citizens left!%n", populationCount);
        return (populationCount / 2);
    }

    private static void reportHarvest(int acresOfFarmland, int harvestCount, int ratFood, int storeCount) {
        System.out.printf("The city now owns %d acres of farmland!%n", acresOfFarmland);
        System.out.printf("The farmers harvested %d bushels of per acre.%n", harvestCount);
        System.out.printf("The rats consumed %d bushels of our bushels.%n", ratFood);
        System.out.printf("You now have %d bushels in store.%n", storeCount);
    }

    private static int[] buyLand(int storeCount) {
        int price = rand.nextInt(17, 26);
        boolean validInput = false;
        int buyAmount = -1;
        int cost = -1;
        while (!validInput) {
            System.out.printf("Land is purchasable at %d bushels per acre and you have %d bushels of grain! \n" +
                    "How many acres do you wish to buy?%n", price, storeCount);
            try {
                buyAmount = Integer.parseInt(scanner.nextLine());
                if (buyAmount >= 0) {
                    cost = price * buyAmount;
                    if (cost > storeCount) {
                        System.out.printf("Hamurabi: Think again. You only have %d bushels of grain.%n", storeCount);
                        System.out.printf("The cost of your requested purchase is %d. %n", cost);
                    } else {
                        validInput = true;
                    }
                }
            } catch (Exception e) {
                System.out.println("Sire! Please provide an appropriate request!");
            }

        }

        System.out.printf("You have purchased %d acres of land for %d bushels of grain. \n" +
                "You now have %d bushels of grain remaining in store.", buyAmount, cost, storeCount - cost);
        return new int[]{cost, buyAmount};
    }

    private static int[] sellLand(int acresOfFarmland) {
        int price = rand.nextInt(12, 21);
        boolean validInput = false;
        int buyAmount = -1;

        while (!validInput) {
            try {
                System.out.printf("Land is selling at %d bushels per acre and you own %d acres! \n" +
                        "How many acres do you wish to sell?%n", price, acresOfFarmland);
                buyAmount = Integer.parseInt(scanner.nextLine());
                if (buyAmount > acresOfFarmland) {
                    System.out.printf("Hamurabi: Think again. You only have %d acres of land and you are attempting" +
                            " to sell %d acres of land? \n", acresOfFarmland, buyAmount);
                } else if (buyAmount >= 0) {
                    validInput = true;
                } else {
                    System.out.println("Sire! Please provide an appropriate request!");
                }

            } catch (Exception e) {
                System.out.println("Sire! Please provide an appropriate request!");
            }
        }
        return new int[]{price, buyAmount};

    }

    private static int[] feedPeople(int storeCount) {
        boolean validInput = false;
        int feedAmount = -1;
        while (!validInput) {
            try {
                System.out.printf("You have %d bushels of grain stored.%nHow many bushels do you wish to feed your people, sire? %n", storeCount);
                feedAmount = Integer.parseInt(scanner.nextLine());
                if (feedAmount > storeCount) {
                    System.out.printf("Hamurabi! Think again, you only have %d bushels of grain, now then... %n", storeCount);
                } else {
                    if (feedAmount >= 0) {
                        validInput = true;
                    } else {
                        System.out.println("Sire! Please provide an appropriate request!");
                    }
                }
            } catch (Exception e) {
                System.out.println("Sire! Please provide an appropriate request!");
            }
        }

        return new int[]{storeCount - feedAmount, feedAmount};


    }

    private static int[] plantCrops(int storeCount, int acresOfFarmLand, int populationCount) {
        boolean validInput = false;
        int acreCount = -1;
        int grainFactorPerLand = 2;
        while (!validInput) {
            try {
                System.out.printf("Hamurabi! You have %d acres of land and %d bushels of grain, how many acres of grain would you like to plant with seed?%n", acresOfFarmLand, storeCount);
                acreCount = Integer.parseInt(scanner.nextLine());

                if (acreCount * grainFactorPerLand > storeCount) {
                    System.out.printf("Hamurabi! Think again, you only have %d bushels of grain, now then... %n", storeCount);
                } else if (acreCount > acresOfFarmLand) {
                    System.out.printf("Sire, you do not have enough land for plot these bushels, now then...%n");
                } else if (acreCount / 10 > populationCount) {
                    System.out.printf("Hamurabi, you only have %d citizens to tend the fields, each can only handle 10 acres each, now then...%n", populationCount);
                } else if (acreCount < 0) {
                    System.out.println("Sire! Please provide an appropriate request!");
                } else {
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.println("Sire! Please provide an appropriate request!");
            }
        }
        return new int[]{acreCount * grainFactorPerLand, acreCount};
    }

    private static int[] harvestCrops(int acresPlanted, int storeCount) {
        int yieldCount = rand.nextInt(1, 6);
        float ratPlagueProbability = 0.5f;
        int ratFood = 0;

        if (Math.random() > ratPlagueProbability) {
            System.out.printf("Oh dear! It appears a plague of rats got into the grain stores. We only appear to have 1/%d of ours stores left" +
                    " meaning we have %d bushels of grain remaining... %n", yieldCount, (storeCount / yieldCount));
            ratFood = storeCount * (yieldCount - 1) / yieldCount;
        }

        System.out.printf("Sire! The harvest had an average yield of %d bushels of grain per acre" +
                " resulting in a total of %d bushels of grain being harvested! %n", yieldCount, acresPlanted * yieldCount);
        return new int[]{acresPlanted * yieldCount, ratFood, 1 / yieldCount * storeCount};


    }

    private static int[] populationChange(int acresOfFarmLand, int storeCount, int populationCount, int foodReserved) {
        int randomNum = rand.nextInt(1, 6);
        int incomingPopulation = ((randomNum * (20 * acresOfFarmLand + storeCount) / populationCount) / 100) + 1;
        int starveCount;
        int grainsRequiredPerPerson = 20;
        int gameOver = 0;
        starveCount = (foodReserved > populationCount * grainsRequiredPerPerson) ? 0 : populationCount - (foodReserved / 20);

        if ((float) (starveCount / populationCount) > 0.45f) {
            gameOver = 1;
        } else {
            populationCount += incomingPopulation - starveCount;
        }

//        System.out.printf("Random number is %d \n Incoming Population is %d \n Fully Fed Population is %d %n", randomNum, incomingPopulation, starveCount);

        return new int[]{incomingPopulation, starveCount, populationCount, gameOver};

    }


}


