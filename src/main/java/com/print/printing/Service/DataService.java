package com.print.printing.Service;

import com.print.printing.model.user;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataService {

    public Context setData(List<user> userList){
        Context context = new Context();
        Map<String,Object> map = new HashMap<>();
        context.setVariables(map);
        return context;
    }
}
