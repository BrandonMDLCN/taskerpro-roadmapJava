package com.tasktraker.taskerpro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tasktraker.taskerpro.utils.LocalDateAdapter;
import com.tasktraker.taskerpro.clases.Task;
import com.tasktraker.taskerpro.utils.AppMessages;
import com.tasktraker.taskerpro.utils.VerificarOCrearArchivo;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TaskerproApplication {
    private static final String HOME = System.getProperty("user.home");
    private static final String RUTA_ARCHIVO_TASKS = HOME + File.separator + "tasks.json";
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).setPrettyPrinting().create();
    
    public static void main(String[] args) {     
        VerificarOCrearArchivo.buscarOCrear(RUTA_ARCHIVO_TASKS);
        List<Task> taskList = cargarTareas();
            System.out.println("TaskerPro Application Started");
            System.out.println(AppMessages.WELCOME_MESSAGE);
            Integer option = 1;
            
        Scanner scanner = new Scanner(System.in);
        int counter = taskList.stream()
              .mapToInt(t -> Integer.parseInt(t.getId().replace(AppMessages.IDENTIFICADOR_TASK, "")))
              .max()
              .orElse(0) + 1;

        while(option != 0) {
            System.out.println("Please select an option:");
            System.out.println("1. Create a new task");
            System.out.println("2. View all tasks");
            System.out.println("3. Update a task");
            System.out.println("4. Delete a task");
            System.out.println("0. Exit");

            String input = scanner.nextLine();
            try {
                    option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                    System.out.println(AppMessages.INVALID_INPUT);
                    continue;
            }

            switch(option) {
                case 1 -> {
                    counter = crearTareaNueva(scanner, taskList, counter);
                }
                case 2 -> {
                    mostrarTareas(scanner, taskList);
                }
                case 3 -> {
                    actualizarTarea(scanner, taskList);
                }
                case 4 -> {
                    eliminarTarea(scanner, taskList);
                }
                case 0 -> System.out.println(AppMessages.EXIT_MESSAGE);
                default -> System.out.println("Invalid option. Please try again.");
            }

        }
    }
    
    private static List<Task> cargarTareas(){
        try (FileReader reader = new FileReader(RUTA_ARCHIVO_TASKS)){
            Type listaType = new TypeToken<ArrayList<Task>>(){}.getType();
            List<Task> loadedTasks = gson.fromJson(reader, listaType);
            return (loadedTasks!=null)?loadedTasks:new ArrayList<>();
        } catch(IOException e){
            return new ArrayList<>();
        }
    }
    
    private static void guardarTareas(List<Task> tasks){
        try (FileWriter writer = new FileWriter(RUTA_ARCHIVO_TASKS)){
            gson.toJson(tasks, writer);
        }catch(IOException e){
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    private static int crearTareaNueva(Scanner scanner, List<Task> taskList, int counter) {
        System.out.println("Introduce Task description");
        String description = scanner.nextLine();
        int nextID = 1;
        if(!taskList.isEmpty()){
            nextID = taskList.stream()
                    .mapToInt(t -> {
                        try{
                            return Integer.valueOf(t.getId().replaceAll("[^0-9]", ""));
                        }catch(NumberFormatException e){
                            return 0;
                        }
                    })
                    .max().getAsInt() + 1;
        }
        LocalDate now = LocalDate.now();
        Task temporalTask = new Task(AppMessages.IDENTIFICADOR_TASK+nextID, description, "to-do", now, now);
        System.out.println(temporalTask.toString());
        taskList.add(temporalTask);
        guardarTareas(taskList);
        System.out.println("Task Saved!");
        return ++counter;
    }

    private static void mostrarTareas(Scanner scanner, List<Task> taskList) {
        System.out.println("Mostrando Tareas");
        System.out.println("Que tareas quieres ver?: 1.) Todas las Tareas 2.) To-Do 3.) In-Progress, 4.) Done");
        System.out.println("Introduce tu opción");
        String cambioEstatus = scanner.nextLine();
        int option = 0;
        try {
            option = Integer.parseInt(cambioEstatus);
        } catch (NumberFormatException e) {
            System.out.println(AppMessages.INVALID_INPUT);  
        }
        switch(option){
            case 1 ->{
                System.out.println("Viewing all tasks...");
                taskList.forEach(t -> System.out.println(t.toString()));
            }
            case 2 ->{
                System.out.println("Viewing all To-Do...");
                taskList.forEach(t->{
                    String estatus = t.getStatus();
                    if(t.getStatus().equalsIgnoreCase("to-do")){
                        System.out.println(t.toString());
                    }
                });
            }
            case 3 ->{
                System.out.println("Viewing all In-Progress...");
                taskList.forEach(t->{
                    String estatus = t.getStatus();
                    if(t.getStatus().equalsIgnoreCase("In-Progress")){
                        System.out.println(t.toString());
                    }
                });
            }
            case 4 ->{
                System.out.println("Viewing all Done...");
                taskList.forEach(t->{
                    String estatus = t.getStatus();
                    if(t.getStatus().equalsIgnoreCase("Done")){
                        System.out.println(t.toString());
                    }
                });
            }
            default -> System.out.println("Invalid option. Please try again.");
        }
        
    }

    private static void actualizarTarea(Scanner scanner, List<Task> taskList) {
        System.out.println("Updating a task...");
        // Code to update a stask would go here
        System.out.println("Select task to update");
        String tastIdToUpdate = scanner.nextLine();
        boolean taskExiste = false;
        for (Task task : taskList) {
            if(task.getId().equals(AppMessages.IDENTIFICADOR_TASK+tastIdToUpdate) || task.getId().equals(tastIdToUpdate)){
                System.out.println(task.toString());
                System.out.println("Cambiar de estatus: 1.) in-progress, 2.) done");
                System.out.println("Introduce tu opción");
                String cambioEstatus = scanner.nextLine();
                int option;
                try {
                    option = Integer.parseInt(cambioEstatus);
                } catch (NumberFormatException e) {
                    System.out.println(AppMessages.INVALID_INPUT);
                    continue;                                                
                }
                if(option == 1){
                    cambioEstatus = "in-progress";
                }
                if(option==2){
                    cambioEstatus = "done";
                }
                task.setStatus(cambioEstatus);
                task.setUpdatedAt(LocalDate.now());
                System.out.println(task.toString());
                taskExiste = true;
            }
        }
        guardarTareas(taskList);
        if(!taskExiste){
            System.out.println("Introduce una tarea existente");
        }
    }

    private static void eliminarTarea(Scanner scanner, List<Task> taskList) {
        System.out.println("Deleting a task...");
        System.out.println("Introduce tarea a eliminar");
        String tareaEliminar = scanner.nextLine();
        int contadorIndice = 0;
        for (Task task : taskList) {
            if(task.getId().equals(AppMessages.IDENTIFICADOR_TASK+tareaEliminar) || task.getId().equals(tareaEliminar)){
                break;
            }
            contadorIndice++;
        }
        taskList.remove(contadorIndice);
        guardarTareas(taskList);
        System.out.println("Task deleted");
    }
}
