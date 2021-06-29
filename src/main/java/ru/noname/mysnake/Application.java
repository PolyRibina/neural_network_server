package ru.noname.mysnake;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import ru.noname.mysnake.api.models.Section;
import ru.noname.mysnake.api.models.SectionsStorage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class Application {
    public static void main(String[] args){
        Javalin app = Javalin.create(JavalinConfig::enableCorsForAllOrigins).start(8000);

        SectionsStorage storage = new SectionsStorage();
        HashMap<String, List<String>> sectionTheme = new HashMap<>();

        app.get("/hello", ctx -> ctx.result("Hello, world!"));

        app.post("/set-section", ctx -> {
            Section section = ctx.bodyAsClass(Section.class);
            storage.addSection(section);
            ctx.json(storage.getSections());
        });
        app.post("/set-theme", ctx -> {
            String section = ctx.queryParam("section", String.class).get();
            String theme = ctx.queryParam("theme", String.class).get();
            List<String> themes = new LinkedList<>();
            if(sectionTheme.containsKey(section)){
                themes.addAll(sectionTheme.get(section));
            }
            themes.add(theme);
            sectionTheme.put(section, themes);
            ctx.json(sectionTheme.get(section));
        });

        app.get("/get-sections", ctx ->{
            System.out.println(storage.getSections());
            ctx.json(storage.getSections());
            });

        app.post("/get-themes", ctx -> {
            String section = ctx.queryParam("section", String.class).get();
            System.out.println(sectionTheme.get(section));
            try {
                if(sectionTheme.get(section)!=null){
                    ctx.json(sectionTheme.get(section));
                }
                else{
                    ctx.status(404);
                }
            } catch (IndexOutOfBoundsException exc) {
                ctx.status(404);
            }
        });

        app.post("/delete-themes", ctx -> {
            String section = ctx.queryParam("section", String.class).get();
            String theme = ctx.queryParam("theme", String.class).get();
            List<String> themes = new LinkedList<>();
            if(sectionTheme.containsKey(section)){
                themes.addAll(sectionTheme.get(section));
            }
            themes.remove(theme);
            sectionTheme.put(section, themes);
            ctx.json(sectionTheme.get(section));
        });

    }
}
