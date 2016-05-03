package cat.proven.resources;


import cat.proven.entities.OwnerClass;
import cat.proven.entities.UserClass;
import cat.proven.services.UserService;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author AMS
 * Exemple Accés al servei GET: 
 * http://localhost:8080/RestFulFindMyPet/restful/users
 */
@Path("users")
public class UsersResource {
    
    UserService serviceUser;
 
    
    public UsersResource(){        
      
    }
    
//http://localhost:8080/RestFulFindMyPet/restful/users/login/admin/admin
    //@Path("login")
    @Path("login/{userName}/{password}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@PathParam("userName")String userName,@PathParam("password")String password){
        Map<String, Object> mapping = new HashMap<>();
        
        serviceUser = new UserService();
        UserClass user = serviceUser.login(userName,password);
        mapping.put("user", user);
        return Response.ok(mapping).build();              
    }
    
    //http://localhost:8080/RestFulFindMyPet/restful/users/test
    @Path("register")
    @POST
    public Response register(@PathParam("user")UserClass user,@PathParam("password")OwnerClass owner){                

        Map<String, Object> mapping = new HashMap<>();
        
        serviceUser = new UserService();
        int result = serviceUser.register(user,owner);
        //mapping.put("user", user);
        return Response.ok(result).build();
    }
    
    /*
    // 
    
    // http://localhost:8080/ActivitatsRestFulCode/v1/students/10/
    @Path("{id}")
    @GET
    public Response findStudentById(@PathParam("id") int id){
        UserClass s = serviceStudent.findById(id);
        if(s == null) 
            return Response.status(Response.Status.NOT_FOUND).build();
        else
            return Response.ok(s).build();
    }
    
    
    @POST
    public Response addStudent(
            @FormParam("name") String name, 
            @FormParam("lastname") String lastname){
        
        if(name == null || name.equals("")){
            ApplicationException ex = new ApplicationException("El paràmetre name és obligatori");
            return Response.status(Response.Status.BAD_REQUEST).entity(ex).build();
        }

        if(lastname == null || lastname.equals("")){
            ApplicationException ex = new ApplicationException("El paràmetre lastname és obligatori");
            return Response.status(Response.Status.BAD_REQUEST).entity(ex).build();
        }
            
        
        UserClass s = new UserClass(0, name, lastname);
        s = serviceStudent.add(s);
        
        return Response.ok(s).build();
    }
    
    @Path("{id}")
    @PUT
    public Response modifyStudent(
            @PathParam("id") int id,
            @FormParam("name") String name, 
            @FormParam("lastname") String lastname){
        
        UserClass s = serviceStudent.findById(id);
        if(s == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        
        if(name == null || name.equals("")){
            ApplicationException ex = new ApplicationException("El paràmetre name és obligatori");
            return Response.status(Response.Status.BAD_REQUEST).entity(ex).build();
        }

        if(lastname == null || lastname.equals("")){
            ApplicationException ex = new ApplicationException("El paràmetre lastname és obligatori");
            return Response.status(Response.Status.BAD_REQUEST).entity(ex).build();
        }
        
        s.setName(name);
        s.setLastname(lastname);
        return Response.ok().build();
        
    }
    
    @DELETE
    @Path("{id}")
    public Response deleteStudent(@PathParam("id") int id){
        UserClass s = serviceStudent.findById(id);
        if(s == null) 
            return Response.status(Response.Status.NOT_FOUND).build();
        else{
            serviceStudent.remove(s);
            return Response.ok().build();
        }            
    }
    
    @Path("{id}/courses")
    @GET
    public Response getCoursesByStudentId(@PathParam("id") int id){
        UserClass s = serviceStudent.findById(id);
        if(s == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        else {
            ArrayList<Course> cursos = s.getCourses();
            GenericEntity<ArrayList<Course>> entity =
                    new GenericEntity<ArrayList<Course>>(cursos) {};
                    
            return Response
                    .ok()
                    .entity(entity)
                    .build();
        }
    }
    
    @Path("{id_student}/courses/{id_course}")
    @POST
    public Response addStudentToCourse(
            @PathParam("id_student") int idStudent, 
            @PathParam("id_course") int idCourse){
        
        UserClass s = serviceStudent.findById(idStudent);
        if(s == null)
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        
        Course c = courseService.findById(idCourse);
        if(c == null)
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        
        s.getCourses().add(c);
        return Response.ok().build();
    }
    */
    
}
