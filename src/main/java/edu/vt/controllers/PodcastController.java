/*
 * Created by Anshika Tyagi, Anubhav Nanda and Het Veera on 2021.12.8
 * Copyright © 2021 Anshika Tyagi, Anubhav Nanda and Het Veera. All rights reserved.
 *
 */

package edu.vt.controllers;

import edu.vt.EntityBeans.Podcast;
import org.primefaces.model.ResponsiveOption;

import javax.annotation.PostConstruct;
//import javax.enterprise.context.ViewScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("PodcastController")

@SessionScoped
public class PodcastController implements Serializable{

    // The List contains podcast filenames, e.g., photo1.png, photo2.png, etc.

    private List<Podcast> podcast1;
    private List<Podcast> podcast2;
    private List<Podcast> podcast3;
    private List<Podcast> podcast4;
    private List<ResponsiveOption> responsiveOptions;

    /*
    The PostConstruct annotation is used on a method that needs to be executed after
    dependency injection is done to perform any initialization. The initialization
    method init() is the first method invoked before this class is put into service.
    */
    @PostConstruct
    public void init() {
        podcast1 = new ArrayList<>();
        podcast2 = new ArrayList<>();
        podcast3 = new ArrayList<>();
        podcast4 = new ArrayList<>();
        podcast1.add(new Podcast("meditation-GqZDgp&ge=s1!c28e8f13e25035dd135fd04038f110f57654c5f5"));
        podcast1.add(new Podcast("meditation-minis-podcast-WzALp4&ge=s1!5705cf83251e1f5c2719f23cdea724d03298b68f"));
        podcast1.add(new Podcast("vendaw14-WdqDPL&ge=s1!cce0e5d21dedb0b04d5e33b0e4bf793e8fc7b1f25"));
        podcast1.add(new Podcast("mediation-8QkpZO&ge=s1!fd4abd6320a379ca3249abd79ad80d15d496b981"));
        podcast1.add(new Podcast("meditation-GqZDgp&ge=s1!cc23c12a5a7d87a9e94d06f1fe4475567ec32b01"));
        podcast1.add(new Podcast("soul-musing-8jDJYv&ge=s1!8ddaac2a5ce7f2cb524214f4b39c29ebf9e4d5be"));

        podcast2.add(new Podcast("positivity-6rbLve&ge=s1!e13257bf1af09a2fea5a095418ac452c3126b8f1"));
        podcast2.add(new Podcast("positivity-8jDRKv&ge=s1!060a030a33b8736ed0863b8f2009b869bba19306"));
        podcast2.add(new Podcast("the-positive-psychology-podcast-WRbOXr&ge=s1!c31217f279bb3990f6949bcdd4cb0b73a8f3cad4"));
        podcast2.add(new Podcast("positive-2cents-WkzRMY&ge=s1!abc1aa16c02eeecd7243c4670fbdd39d3cd8da94"));
        podcast2.add(new Podcast("be-positive-6LLppa&ge=s1!b740b52846b9763455c9a4ae7ceb8a8c9869f99a"));
        podcast2.add(new Podcast("a-mindful-moment-Wk0aJy&ge=s1!fc4d0e38a24d9e89786bbc9794fd40955cfb4f8d"));

        podcast3.add(new Podcast("self-love-Gqy3BP&ge=s1!9a00c42c66f23075f1cc4c8c6b702d2f9a39b92d"));
        podcast3.add(new Podcast("on-the-mind-6neJYE&amp;ge=s1!fe1c0922bd0fca17cf99c33e02462f232357d85e"));
        podcast3.add(new Podcast("self-love-Gqy3BP&ge=s1!27eb1d96c64facda79a8b10d75f994ce3e7e5372"));
        podcast3.add(new Podcast("mind-my-mindfulness-Wx55Oo&amp;ge=s1!79bbe7816831b1d958b53d45d6314de515174f3d"));
        podcast3.add(new Podcast("self-love-85wZbz&ge=s1!5733a96345b0a3ba85f0ec7557ecb50f9c9d275a"));
        podcast3.add(new Podcast("super-soul-G2wMy3&ge=s1!9ff6638694e140d5320911a1c77c9beb3be3f64"));


        podcast4.add(new Podcast("creative-spirit-playful-disciplin-G2pmER&ge=s1!c8e1088a6a89f168b182f17018a560164ad73d79"));
        podcast4.add(new Podcast("slow-radio-WDp4kw&ge=s1!4c81cd55aef56dc33cdfed24f429eb9af823c874"));
        podcast4.add(new Podcast("a-is-for-anxiety-6N33yz&ge=s1!0edb817cdd5123148d52dfb889aa4a011ec59fb3"));
        podcast4.add(new Podcast("calme-GE9EnZ&ge=s1!47e5075b23c00bffc078720c75997add2d86899e"));
        podcast4.add(new Podcast("anxiety-WdV2y7&ge=s1!6d8167692c6debed499c2c12d02b57c09627c32f"));
        podcast4.add(new Podcast("creative-spirit-playful-disciplin-G2pmER&ge=s1!f38a20188c0275d5d9b2b6251e71576d29cc43b8"));

        responsiveOptions = new ArrayList<>();
        responsiveOptions.add(new ResponsiveOption("1024px", 3, 3));
        responsiveOptions.add(new ResponsiveOption("768px", 2, 2));
        responsiveOptions.add(new ResponsiveOption("560px", 1, 1));}

    /*
    =============
    Getter Method
    =============
     */

    public List<Podcast> getPodcast1() {
        return podcast1;
    }

    public void setPodcast1(List<Podcast> podcast1) {
        this.podcast1 = podcast1;
    }

    public List<Podcast> getPodcast2() {
        return podcast2;
    }

    public void setPodcast2(List<Podcast> podcast2) {
        this.podcast2 = podcast2;
    }

    public List<Podcast> getPodcast3() {
        return podcast3;
    }


    public void setPodcast3(List<Podcast> podcast3) {
        this.podcast3 = podcast3;
    }
    public List<Podcast> getPodcast4() {
        return podcast4;
    }

    public void setPodcast4(List<Podcast> podcast4) {
        this.podcast4 = podcast4;
    }



    public List<ResponsiveOption> getResponsiveOptions() {
        return responsiveOptions;
    }

    public void setResponsiveOptions(List<ResponsiveOption> responsiveOptions) {
        this.responsiveOptions = responsiveOptions;
    }
}
