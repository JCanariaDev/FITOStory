package com.example.fitostory;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import java.util.ArrayList;

public class InitializeClass {
    DataBaseHelper dataBaseHelper;
    StoryClass storyClass;
    Context context;
    private TextView creatureNameTextView;
    private TextView storyTitleTextView;
    private TextView storyTextTextView;
    private String storyTitle;

    public InitializeClass(DataBaseHelper dataBaseHelper, TextView creatureNameTextView,
                           TextView storyTitleTextView, TextView storyTextTextView,
                           String storyTitle, Context context){
        this.dataBaseHelper = dataBaseHelper;
        this.creatureNameTextView = creatureNameTextView;
        this.storyTitleTextView = storyTitleTextView;
        this.storyTextTextView = storyTextTextView;
        this.storyTitle = storyTitle;
        this.context = context;
    }

    public InitializeClass(DataBaseHelper dataBaseHelper){
        this.dataBaseHelper = dataBaseHelper;
    }

    public void setUpStory(){
        if (dataBaseHelper.getAllQuizzes().isEmpty()) {
            // Define questions, options, and answers
            String[] creationQuestions = new String[5];
            String[][] creationOptions = new String[5][4];
            String[] creationAnswers = new String[5];

            creationQuestions[0] = "Ano ang unang anyo ng mundo ayon sa kwento?";
            creationOptions[0][0] = "May lupa at kabundukan";
            creationOptions[0][1] = "Langit at dagat lamang";
            creationOptions[0][2] = "Mga tao at hayop";
            creationOptions[0][3] = "Bituin at buwan";
            creationAnswers[0] = "Langit at dagat lamang";

            creationQuestions[1] = "Bakit lumitaw ang mga isla?";
            creationOptions[1][0] = "Galing sa pagsabog ng bulkan";
            creationOptions[1][1] = "Itinapon ng langit upang pigilan ang dagat";
            creationOptions[1][2] = "Gawa ng mga diyos";
            creationOptions[1][3] = "Nabuo mula sa ulap";
            creationAnswers[1] = "Itinapon ng langit upang pigilan ang dagat";

            creationQuestions[2] = "Paano nabuo ang unang lalaki at babae?";
            creationOptions[2][0] = "Galing sa bato";
            creationOptions[2][1] = "Hinipan ng hangin";
            creationOptions[2][2] = "Lumabas sa kawayan na tinuka ng ibon";
            creationOptions[2][3] = "Gawa ng kidlat";
            creationAnswers[2] = "Lumabas sa kawayan na tinuka ng ibon";

            creationQuestions[3] = "Ano ang ginawa ng ama sa mga anak na tamad?";
            creationOptions[3][0] = "Pinatulog sa labas";
            creationOptions[3][1] = "Pinakain ng gulay";
            creationOptions[3][2] = "Hinampas gamit ang kahoy";
            creationOptions[3][3] = "Kinulong sa silid";
            creationAnswers[3] = "Hinampas gamit ang kahoy";

            creationQuestions[4] = "Saan nagmula ang mga puti ayon sa kwento?";
            creationOptions[4][0] = "Naglakbay sa dagat at bumalik ang kanilang mga anak";
            creationOptions[4][1] = "Galing sa bituin";
            creationOptions[4][2] = "Umakyat sa langit";
            creationOptions[4][3] = "Nagtago sa ilalim ng lupa";
            creationAnswers[4] = "Naglakbay sa dagat at bumalik ang kanilang mga anak";

            // Add the quiz
            dataBaseHelper.addQuiz(new QuizClass(5, R.drawable.image5, "The Creation Story", creationQuestions, creationOptions, creationAnswers));

            // Add the Library (category) - Tagalog
            dataBaseHelper.addLibrary(new LibraryClass(5, "Tagalog", R.drawable.image5));

            // Add the story
            dataBaseHelper.addStory(new StoryClass(
                    5,
                    R.drawable.image5,
                    "Tagalog", // LibraryClass/category
                    "The Creation Story", // StoryClass title
                    "When the world first began there was no land, but only the sea and the sky, and between them was a kite. One day the bird, which had nowhere to light, grew tired of flying about, so she stirred up the sea until it threw its waters against the sky. The sky, in order to restrain the sea, showered upon it many islands until it could no longer rise, but ran back and forth. Then the sky ordered the kite to light on one of the islands to build her nest and to leave the sea and the sky in peace.\n\nNow at this time the land breeze and the sea breeze were married, and they had a child which was a bamboo. One day, when this bamboo was floating about on the water, it struck the feet of the kite, which was on the beach. The bird, angry that anything should strike it, pecked at the bamboo, and out of one section came a man and from the other a woman.\n\nThen the earthquake called on all the birds and fish to see what should be done with these two, and it was decided that they should marry. Many children were born to the couple, and from them came all the different races of people.\n\nAfter a while, the parents grew very tired of having so many idle and useless children around, and they wished to be rid of them, but they knew of no place to send them. Time went on, and the children became so numerous that the parents enjoyed no peace. One day, in desperation, the father seized a stick and began beating them on all sides.\n\nThis so frightened the children that they fled in different directions, seeking hidden rooms in the house—some concealed themselves in the walls, some ran outside, while others hid in the fireplace, and several fled to the sea.\n\nNow it happened that those who went into the hidden rooms of the house later became the chiefs of the Islands; and those who concealed themselves in the walls became slaves. Those who ran outside were free men; and those who hid in the fireplace became negroes; while those who fled to the sea were gone many years, and when their children came back, they were the white people.",
                    new QuizClass(5, R.drawable.image5, "The Creation Story 1", creationQuestions, creationOptions, creationAnswers)
            ));

            // Add Library category (Tagalog)
            //dataBaseHelper.addLibrary(new LibraryClass(1, "Tagalog", R.drawable.image4));

            // Prepare the quiz content
            String[] questions = new String[]{
                    "What kind of family did Benito come from?",
                    "Why did Benito want to go to the palace?",
                    "What did Benito dream about when he heard about the young king?",
                    "Why were Benito’s parents hesitant to let him go?",
                    "What was Benito's first role at the palace?",
                    "What was the king's task for Benito?",
                    "What was Benito's reaction to the king’s order?",
                    "What did Benito find in the forest?",
                    "What did the bird promise Benito?",
                    "What does the story suggest about helping others?"
            };

            String[][] options = new String[][]{
                    {"Rich and powerful", "Poor and struggling", "Royal family", "Merchant family"},
                    {"To become a soldier", "To see the palace", "To ask for work", "To meet the king"},
                    {"Living with animals", "Living in the mountains", "Becoming a king", "Becoming a servant"},
                    {"They were afraid of the king", "They wanted him to be a farmer", "They didn't like traveling", "They wanted him to study first"},
                    {"He became a prince", "He became a cook", "He became a servant", "He became a guard"},
                    {"To build a palace", "To fetch a princess", "To find treasure", "To fight a monster"},
                    {"He cried and refused", "He ran away", "He bravely accepted", "He asked for help"},
                    {"A treasure chest", "A wounded deer", "A large bird tied with strings", "A magic tree"},
                    {"That it would bring him food", "That it would help him when called", "That it would make him king", "That it would take him home"},
                    {"Helping others brings reward", "Don’t talk to strangers", "Work hard and stay home", "Never enter a forest"}
            };

            String[] answers = new String[]{
                    "Poor and struggling",
                    "To ask for work",
                    "Becoming a king",
                    "They were afraid of the king",
                    "He became a servant",
                    "To fetch a princess",
                    "He bravely accepted",
                    "A large bird tied with strings",
                    "That it would help him when called",
                    "Helping others brings reward"
            };

            // Create and insert the quiz
            QuizClass quiz = new QuizClass(1, R.drawable.image4, "The Story of Benito", questions, options, answers);
            dataBaseHelper.addQuiz(quiz);

            // Insert the story with the quiz attached
            dataBaseHelper.addStory(new StoryClass(
                    1,
                    R.drawable.image4,
                    "Tagalog", // Category
                    "The Story of Benito", // Story title
                    "Benito was an only son who lived with his father and mother in a little village. They were very poor, and as the boy grew older and saw how hard his parents struggled for their scanty living he often dreamed of a time when he might be a help to them.\n" +
                            "\n" +
                            "One evening when they sat eating their frugal meal of rice the father told about a young king who lived in a beautiful palace some distance from their village, and the boy became very much interested. That night when the house was dark and quiet and Benito lay on his mat trying to sleep, thoughts of the young king repeatedly came to his mind, and he wished he were a king that he and his parents might spend the rest of their lives in a beautiful palace.\n" +
                            "\n" +
                            "The next morning he awoke with a new idea. He would go to the king and ask for work, that he might in that way be able to help his father and mother. He was a long time in persuading his parents to allow him to go, however, for it was a long journey, and they feared that the king might not be gracious. But at last they gave their consent, and the boy started out The journey proved tiresome. After he reached the palace, he was not at first permitted to see the king. But the boy being very earnest at last secured a place as a servant.[190]\n" +
                            "\n" +
                            "It was a new and strange world to Benito who had known only the life of a little village. The work was hard, but he was happy in thinking that now he could help his father and mother. One day the king sent for him and said: “I want you to bring to me a beautiful princess who lives in a land across the sea. Go at once, and if you fail you shall be punished severely,”\n" +
                            "\n" +
                            "The boy’s heart sank within him, for he did not know what to do. But he answered as bravely as possible, “I will, my lord,” and left the king’s chamber. He at once set about preparing things for a long journey, for he was determined to try at least to fulfil the command.\n" +
                            "\n" +
                            "When all was ready Benito started. He had not gone far before he came to a thick forest, where he saw a large bird bound tightly with strings.\n" +
                            "\n" +
                            "“Oh, my friend,” pleaded the bird, “please free me from these bonds, and I will help you whenever you call on me.”\n" +
                            "\n" +
                            "Benito quickly released the bird, and it flew away calling back to him that its name was Sparrow-hawk.\n" +
                            "\n" +
                            "Benito continued his journey till he came to the sea. Unable to find a way of crossing, he stopped and gazed sadly out over the waters, thinking of the king’s threat if he failed. Suddenly he saw swimming toward him the King of the Fishes who asked:\n" +
                            "\n" +
                            "“Why are you so sad?”\n" +
                            "\n" +
                            "“I wish to cross the sea to find the beautiful Princess,” answered the boy. “Well, get on my back,” said the Fish, “and I will carry you across.”\n" +
                            "\n" +
                            "So Benito stepped on his back and was carried to the other shore.\n" +
                            "\n" +
                            "Soon he met a strange woman who inquired what it was he sought, and when he had told her she said:\n" +
                            "\n" +
                            "“The Princess is kept in a castle guarded by giants. Take this magic sword, for it will kill instantly whatever it touches.” And she handed him the weapon.\n" +
                            "\n" +
                            "Benito was more than grateful for her kindness and went on full of hope. As he approached the castle he could see that it was surrounded by many giants, and as soon as they saw him they ran out to seize him, but they went unarmed for they saw that he was a mere boy. As they approached he touched those in front with his sword, and one by one they fell dead. Then the others ran away in a panic, and left the castle unguarded. Benito entered, and when he had told the Princess of his errand, she was only too glad to escape from her captivity and she set out at once with him for the palace of the king.\n" +
                            "\n" +
                            "At the seashore the King of the Fishes was waiting for them, and they had no difficulty in crossing the sea and then in journeying through the thick forest to the palace, where they were received with great rejoicing. After a time the King asked the Princess to become his wife, and she replied:\n" +
                            "\n" +
                            "“I will, O King, if you will get the ring I lost in the sea as I was crossing it”\n" +
                            "\n" +
                            "The King immediately thought of Benito, and sending for him he commanded him to find the ring which [192]had been lost on the journey from the land of the giants.\n" +
                            "\n" +
                            "It seemed a hopeless task to the boy, but, anxious to obey his master, he started out. At the seaside he stopped and gazed over the waters until, to his great delight, he saw his friend, the King of the Fishes, swimming toward him. When he had been told of the boy’s troubles, the great fish said: “I will see if I can help you,” and he summoned all his subjects to him. When they came he found that one was missing, and he sent the others in search of it. They found it under a stone so full that it could not swim, and the larger ones took it by the tail and dragged it to the King.\n" +
                            "\n" +
                            "“Why did you not come when you were called?” inquired the King Fish.\n" +
                            "\n" +
                            "“I have eaten so much that I cannot swim,” replied the poor fish.\n" +
                            "\n" +
                            "Then the King Fish, suspecting the truth, ordered it cut open, and inside they found the lost ring. Benito was overjoyed at this, and expressing his great thanks, hastened with the precious ring to his master.\n" +
                            "\n" +
                            "The King, greatly pleased, carried the ring to the Princess and said:\n" +
                            "\n" +
                            "“Now that I have your ring will you become my wife?”\n" +
                            "\n" +
                            "“I will be your wife,” replied the Princess, “if you will find my earring that I lost in the forest as I was journeying with Benito.”\n" +
                            "\n" +
                            "Again the King sent for Benito, and this time he commanded him to find the earring. The boy was [193]very weary from his long journeys, but with no complaint he started out once more. Along the road through the thick forest he searched carefully, but with no reward. At last, tired and discouraged, he sat down under a tree to rest.\n" +
                            "\n" +
                            "Suddenly there appeared before him a mouse of great size, and he was surprised to find that it was the King of Mice.\n" +
                            "\n" +
                            "“Why are you so sad?” asked the King Mouse.\n" +
                            "\n" +
                            "“Because,” answered the boy, “I cannot find an earring which the Princess lost as we were going through the forest together.”\n" +
                            "\n" +
                            "“I will help you,” said the Mouse, and he summoned all his subjects.\n" +
                            "\n" +
                            "When they assembled it was found that one little mouse was missing, and the King sent the others to look for him. In a small hole among the bamboo trees they found him, and he begged to be left alone, for, he said, he was so full that he could not walk. Nevertheless they pulled him along to their master, who, upon finding that there was something hard inside the mouse, ordered him cut open; and inside they found the missing earring.\n" +
                            "\n" +
                            "Benito at once forgot his weariness, and after expressing his great thanks to the King Mouse he hastened to the palace with the prize. The King eagerly seized the earring and presented it to the Princess, again asking her to be his wife.\n" +
                            "\n" +
                            "“Oh, my King,” replied the Princess, “I have one more request to make. Only grant it and I will be your wife forever.”[194]\n" +
                            "\n" +
                            "The King, believing that now with the aid of Benito he could grant anything, inquired what it was she wished, and she replied:\n" +
                            "\n" +
                            "“Get me some water from heaven and some from the lower world, and I shall ask nothing more.”\n" +
                            "\n" +
                            "Once more the King called Benito and sent him on the hardest errand of all.\n" +
                            "\n" +
                            "The boy went out not knowing which way to turn, and while he was in a deep study his weary feet led him to the forest. Suddenly he thought of the bird who had promised to help him, and he called, “Sparrowhawk!” There was a rustle of wings, and the bird swooped down. He told it of his troubles and it said:\n" +
                            "\n" +
                            "“I will get the water for you.”\n" +
                            "\n" +
                            "Then Benito made two light cups of bamboo which he fastened to the bird’s legs, and it flew away. All day the boy waited in the forest, and just as night was coming on the bird returned with both cups full. The one on his right foot, he told Benito, was from heaven, and that on his left was from the lower world. The boy unfastened the cups, and then, as he was thanking the bird, he noticed that the journey had been too much for it and that it was dying. Filled with sorrow for his winged friend, he waited and carefully buried it, and then he hastened to the palace with the precious water.\n" +
                            "\n" +
                            "When the Princess saw that her wish had been fulfilled she asked the King to cut her in two and pour over her the water from heaven. The King was not able to do this, so she cut herself, and then as he poured [195]the water over her he beheld her grow into the most beautiful woman he had ever seen.\n" +
                            "\n" +
                            "Eager to become handsome himself, the King then begged her to pour over him the water from the other cup. He cut himself, and she did as he requested, but immediately there arose a creature most ugly and horrible to look upon, which soon vanished out of sight. Then the Princess called Benito and told him that because he had been so faithful to his master and so kind to her, she chose him for her husband.\n" +
                            "\n" +
                            "They were married amid great festivities and became king and queen of that broad and fertile land. During all the great rejoicing, however, Benito never forgot his parents. One of the finest portions of his kingdom he gave to them, and from that time they all lived in great happiness.",
                    quiz
            ));


        }
    }

    public void setUpStoryDetail(){
        ArrayList<StoryClass> storyClasses = dataBaseHelper.getAllStory();

        for (StoryClass sC: storyClasses){
            if (sC.getStoryTitle().equals(storyTitle)){
                storyClass = sC;
            }
        }

        creatureNameTextView.setText(storyClass.getCreatureName());
        storyTitleTextView.setText(storyClass.getStoryTitle());
        storyTextTextView.setText(storyClass.getStoryText());
    }
}
