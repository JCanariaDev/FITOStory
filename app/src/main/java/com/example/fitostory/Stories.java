package com.example.fitostory;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Stories {
    DataBaseHelper dataBaseHelper;

    public Stories(DataBaseHelper dataBaseHelper){
        this.dataBaseHelper = dataBaseHelper;
    }

    public void setUp(){
        if (dataBaseHelper.getAllQuizzes().isEmpty()) {
            // Monkey and Turtle Quiz
            String[] monkeyTurtleQuestions = new String[5];
            String[][] monkeyTurtleOptions = new String[5][4];
            String[] monkeyTurtleAnswers = new String[5];

            monkeyTurtleQuestions[0] = "Why was the monkey sad at the beginning of the story?";
            monkeyTurtleOptions[0][0] = "He lost his family";
            monkeyTurtleOptions[0][1] = "He was very hungry";
            monkeyTurtleOptions[0][2] = "He was injured";
            monkeyTurtleOptions[0][3] = "He was lost";
            monkeyTurtleAnswers[0] = "He was very hungry";

            monkeyTurtleQuestions[1] = "What did the turtle and monkey steal to plant?";
            monkeyTurtleOptions[1][0] = "Banana plants";
            monkeyTurtleOptions[1][1] = "Coconut trees";
            monkeyTurtleOptions[1][2] = "Rice seeds";
            monkeyTurtleOptions[1][3] = "Mango saplings";
            monkeyTurtleAnswers[1] = "Banana plants";

            monkeyTurtleQuestions[2] = "Where did the monkey plant his banana plant?";
            monkeyTurtleOptions[2][0] = "In a hole in the ground";
            monkeyTurtleOptions[2][1] = "Near the river";
            monkeyTurtleOptions[2][2] = "In a tree";
            monkeyTurtleOptions[2][3] = "In the mountain";
            monkeyTurtleAnswers[2] = "In a tree";

            monkeyTurtleQuestions[3] = "How did the turtle trick the other monkeys at the end?";
            monkeyTurtleOptions[3][0] = "He pretended to be dead";
            monkeyTurtleOptions[3][1] = "He told them water was good for monkeys";
            monkeyTurtleOptions[3][2] = "He disguised himself as a monkey";
            monkeyTurtleOptions[3][3] = "He bribed them with bananas";
            monkeyTurtleAnswers[3] = "He told them water was good for monkeys";

            monkeyTurtleQuestions[4] = "What happened to the monkeys who jumped into the water?";
            monkeyTurtleOptions[4][0] = "They learned to swim";
            monkeyTurtleOptions[4][1] = "They caught many lobsters";
            monkeyTurtleOptions[4][2] = "They transformed into fish";
            monkeyTurtleOptions[4][3] = "They drowned";
            monkeyTurtleAnswers[4] = "They drowned";

            // Benito Quiz
            String[] benitoQuestions = new String[5];
            String[][] benitoOptions = new String[5][4];
            String[] benitoAnswers = new String[5];

            benitoQuestions[0] = "Why did Benito want to work for the king?";
            benitoOptions[0][0] = "To become rich himself";
            benitoOptions[0][1] = "To marry the princess";
            benitoOptions[0][2] = "To help his poor parents";
            benitoOptions[0][3] = "To overthrow the kingdom";
            benitoAnswers[0] = "To help his poor parents";

            benitoQuestions[1] = "Who helped Benito cross the sea?";
            benitoOptions[1][0] = "The Princess";
            benitoOptions[1][1] = "The King of Fishes";
            benitoOptions[1][2] = "The Sparrow-hawk";
            benitoOptions[1][3] = "The King of Mice";
            benitoAnswers[1] = "The King of Fishes";

            benitoQuestions[2] = "What did the strange woman give Benito?";
            benitoOptions[2][0] = "A magic sword";
            benitoOptions[2][1] = "A flying carpet";
            benitoOptions[2][2] = "A potion of invisibility";
            benitoOptions[2][3] = "A map to the castle";
            benitoAnswers[2] = "A magic sword";

            benitoQuestions[3] = "Who helped Benito find the lost earring?";
            benitoOptions[3][0] = "The King of Fishes";
            benitoOptions[3][1] = "The strange woman";
            benitoOptions[3][2] = "The King of Mice";
            benitoOptions[3][3] = "The Sparrow-hawk";
            benitoAnswers[3] = "The King of Mice";

            benitoQuestions[4] = "What was the Princess's final request?";
            benitoOptions[4][0] = "A golden crown";
            benitoOptions[4][1] = "Water from heaven and the lower world";
            benitoOptions[4][2] = "A thousand soldiers";
            benitoOptions[4][3] = "The king's throne";
            benitoAnswers[4] = "Water from heaven and the lower world";

            // Add the quizzes
            dataBaseHelper.addQuiz(new QuizClass(1, R.drawable.monkey_turtle, "The Monkey and the Turtle", monkeyTurtleQuestions, monkeyTurtleOptions, monkeyTurtleAnswers));
            dataBaseHelper.addQuiz(new QuizClass(2, R.drawable.the_story_of_bennito, "The Story of Benito", benitoQuestions, benitoOptions, benitoAnswers));

            // Add the Libraries (categories)
            dataBaseHelper.addLibrary(new LibraryClass(1, "Filipino Fables", R.drawable.filipino_fables));
            dataBaseHelper.addLibrary(new LibraryClass(2, "Filipino Folktales", R.drawable.filipino_forltakes));

            // Add the Monkey and Turtle story
            dataBaseHelper.addStory(new StoryClass(
                    1,
                    R.drawable.monkey_turtle,
                    "Filipino Fables", // LibraryClass/category
                    "The Monkey and the Turtle", // StoryClass title
                    "A monkey, looking very sad and dejected, was walking along the bank of the river one day when he met a turtle.\n\n\"How are you?\" asked the turtle, noticing that he looked sad.\n\nThe monkey replied, \"Oh, my friend, I am very hungry. The squash of Mr. Farmer were all taken by the other monkeys, and now I am about to die from want of food.\"\n\n\"Do not be discouraged,\" said the turtle; \"take a bolo and follow me and we will steal some banana plants.\"\n\nSo they walked along together until they found some nice plants which they dug up, and then they looked for a place to set them. [[CHOICE:Help the turtle plant the banana in the ground|Help the monkey plant the banana in a tree]]\n",
                    new QuizClass(1, R.drawable.monkey_turtle, "The Monkey and the Turtle", monkeyTurtleQuestions, monkeyTurtleOptions, monkeyTurtleAnswers),
                    "You helped the turtle plant the banana in fertile soil. It grew tall and produced many fruit. When harvest time came, the monkey climbed to get some bananas but refused to share with the turtle. You watched as the turtle, angry at this betrayal, set sharp bamboo spikes around the tree. When the turtle called 'Crocodile is coming!', the startled monkey fell onto the spikes. The turtle dried the monkey meat and sold it to other monkeys. When they discovered they were eating one of their own, they tried to kill the turtle, but he tricked them into drowning themselves in the water. Justice was served, though at a great cost.",
                    "You helped the monkey plant the banana in the tree where it withered and died. The monkey became angry and stole all the turtle's bananas. When the turtle tried to trick the monkey, things got worse. The monkey told his friends, and they caught the turtle. They threw him into the water, where he tricked them into drowning. The cycle of revenge left both sides with loss and tragedy."
            ));

            // Add the Benito story
            dataBaseHelper.addStory(new StoryClass(
                    2,
                    R.drawable.the_story_of_bennito,
                    "Filipino Folktales", // LibraryClass/category
                    "The Story of Benito", // StoryClass title
                    "Benito was an only son who lived with his father and mother in a little village. They were very poor, and as the boy grew older and saw how hard his parents struggled for their scanty living he often dreamed of a time when he might be a help to them.\n\nOne evening when they sat eating their frugal meal of rice the father told about a young king who lived in a beautiful palace some distance from their village, and the boy became very much interested. That night when the house was dark and quiet and Benito lay on his mat trying to sleep, thoughts of the young king repeatedly came to his mind, and he wished he were a king that he and his parents might spend the rest of their lives in a beautiful palace.\n\nThe next morning he awoke with a new idea. He would go to the king and ask for work, that he might in that way be able to help his father and mother. [[CHOICE:Encourage Benito to go on the journey|Advise Benito to stay with his parents]]\n",
                    new QuizClass(2, R.drawable.the_story_of_bennito, "The Story of Benito", benitoQuestions, benitoOptions, benitoAnswers),
                    "You encouraged Benito on his journey. The journey proved tiresome. After he reached the palace, he was not at first permitted to see the king. But the boy being very earnest at last secured a place as a servant.\n\nIt was a new and strange world to Benito who had known only the life of a little village. The work was hard, but he was happy in thinking that now he could help his father and mother. One day the king sent for him and said:\n'I want you to bring to me a beautiful princess who lives in a land across the sea. Go at once, and if you fail you shall be punished severely.'\n\nThe boy's heart sank within him, for he did not know what to do. But he answered as bravely as possible, 'I will, my lord,' and left the king's chamber. He at once set about preparing things for a long journey, for he was determined to try at least to fulfil the command.\n\nWhen all was ready Benito started. He had not gone far before he came to a thick forest, where he saw a large bird bound tightly with strings.\n\n'Oh, my friend,' pleaded the bird, 'please free me from these bonds, and I will help you whenever you call on me.'\n\nBenito quickly released the bird, and it flew away calling back to him that its name was Sparrow-hawk.\n\nBenito continued his journey till he came to the sea. Unable to find a way of crossing, he stopped and gazed sadly out over the waters, thinking of the king's threat if he failed. Suddenly he saw swimming toward him the King of the Fishes who asked:\n\n'Why are you so sad?'\n\n'I wish to cross the sea to find the beautiful Princess,' answered the boy.\n\n'Well, get on my back,' said the Fish, 'and I will carry you across.'\n\nSo Benito stepped on his back and was carried to the other shore.\n\nSoon he met a strange woman who inquired what it was he sought, and when he had told her she said:\n\n'The Princess is kept in a castle guarded by giants. Take this magic sword, for it will kill instantly whatever it touches.' And she handed him the weapon.\n\nBenito was more than grateful for her kindness and went on full of hope. As he approached the castle he could see that it was surrounded by many giants, and as soon as they saw him they ran out to seize him, but they went unarmed for they saw that he was a mere boy. As they approached he touched those in front with his sword, and one by one they fell dead. Then the others ran away in a panic, and left the castle unguarded. Benito entered, and when he had told the Princess of his errand, she was only too glad to escape from her captivity and she set out at once with him for the palace of the king.\n\nAt the seashore the King of the Fishes was waiting for them, and they had no difficulty in crossing the sea and then in journeying through the thick forest to the palace, where they were received with great rejoicing. After a time the King asked the Princess to become his wife, and she replied:\n\n'I will, O King, if you will get the ring I lost in the sea as I was crossing it' The King immediately thought of Benito, and sending for him he commanded him to find the ring which had been lost on the journey from the land of the giants.\n\nIt seemed a hopeless task to the boy, but, anxious to obey his master, he started out. At the seaside he stopped and gazed over the waters until, to his great delight, he saw his friend, the King of the Fishes, swimming toward him. When he had been told of the boy's troubles, the great fish said: 'I will see if I can help you,' and he summoned all his subjects to him. When they came he found that one was missing, and he sent the others in search of it. They found it under a stone so full that it could not swim, and the larger ones took it by the tail and dragged it to the King.\n\n'Why did you not come when you were called?' inquired the King Fish.\n\n'I have eaten so much that I cannot swim,' replied the poor fish.\n\nThen the King Fish, suspecting the truth, ordered it cut open, and inside they found the lost ring. Benito was overjoyed at this, and expressing his great thanks, hastened with the precious ring to his master.\n\nThe King, greatly pleased, carried the ring to the Princess and said:\n\n'Now that I have your ring will you become my wife?'\n\n'I will be your wife,' replied the Princess, 'if you will find my earring that I lost in the forest as I was journeying with Benito.'\n\nAgain the King sent for Benito, and this time he commanded him to find the earring. The boy was very weary from his long journeys, but with no complaint he started out once more. Along the road through the thick forest he searched carefully, but with no reward. At last, tired and discouraged, he sat down under a tree to rest.\n\nSuddenly there appeared before him a mouse of great size, and he was surprised to find that it was the King of Mice.\n\n'Why are you so sad?' asked the King Mouse.\n\n'Because,' answered the boy, 'I cannot find an earring which the Princess lost as we were going through the forest together.'\n\n'I will help you,' said the Mouse, and he summoned all his subjects.\n\nWhen they assembled it was found that one little mouse was missing, and the King sent the others to look for him. In a small hole among the bamboo trees they found him, and he begged to be left alone, for, he said, he was so full that he could not walk. Nevertheless they pulled him along to their master, who, upon finding that there was something hard inside the mouse, ordered him cut open; and inside they found the missing earring.\n\nBenito at once forgot his weariness, and after expressing his great thanks to the King Mouse he hastened to the palace with the prize. The King eagerly seized the earring and presented it to the Princess, again asking her to be his wife.\n\n'Oh, my King,' replied the Princess, 'I have one more request to make. Only grant it and I will be your wife forever.'\n\nThe King, believing that now with the aid of Benito he could grant anything, inquired what it was she wished, and she replied:\n\n'Get me some water from heaven and some from the lower world, and I shall ask nothing more.'\n\nOnce more the King called Benito and sent him on the hardest errand of all.\n\nThe boy went out not knowing which way to turn, and while he was in a deep study his weary feet led him to the forest. Suddenly he thought of the bird who had promised to help him, and he called, 'Sparrowhawk!' There was a rustle of wings, and the bird swooped down. He told it of his troubles and it said:\n\n'I will get the water for you.'\n\nThen Benito made two light cups of bamboo which he fastened to the bird's legs, and it flew away. All day the boy waited in the forest, and just as night was coming on the bird returned with both cups full. The one on his right foot, he told Benito, was from heaven, and that on his left was from the lower world. The boy unfastened the cups, and then, as he was thanking the bird, he noticed that the journey had been too much for it and that it was dying. Filled with sorrow for his winged friend, he waited and carefully buried it, and then he hastened to the palace with the precious water.\n\nWhen the Princess saw that her wish had been fulfilled she asked the King to cut her in two and pour over her the water from heaven. The King was not able to do this, so she cut herself, and then as he poured the water over her he beheld her grow into the most beautiful woman he had ever seen.\n\nEager to become handsome himself, the King then begged her to pour over him the water from the other cup. He cut himself, and she did as he requested, but immediately there arose a creature most ugly and horrible to look upon, which soon vanished out of sight. Then the Princess called Benito and told him that because he had been so faithful to his master and so kind to her, she chose him for her husband.\n\nThey were married amid great festivities and became king and queen of that broad and fertile land. During all the great rejoicing, however, Benito never forgot his parents. One of the finest portions of his kingdom he gave to them, and from that time they all lived in great happiness.",
                    "You advised Benito to stay home with his parents. They remained in poverty, struggling each day to survive. Without Benito's help, the princess remained captive in the castle guarded by giants. The greedy king, desperate to find someone to complete his impossible tasks, became increasingly cruel to his servants. Benito's potential to become a great and kind ruler was never realized, and he always wondered what might have been if he had taken the journey."
            ));
        }
    }
}