// package com.permovdb.permovdb.security;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.MethodParameter;
// import org.springframework.stereotype.Component;
// import org.springframework.web.bind.support.WebDataBinderFactory;
// import org.springframework.web.context.request.NativeWebRequest;
// import org.springframework.web.method.support.HandlerMethodArgumentResolver;
// import org.springframework.web.method.support.ModelAndViewContainer;

// import com.permovdb.permovdb.annotation.CurrentUser;

// import jakarta.servlet.http.HttpServletRequest;

// @Component
// public class CurrentUserResolver implements HandlerMethodArgumentResolver {

//     @Autowired
//     private JwtUtil jwtUtil;

//     @Override
//     public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//             NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//         HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

//         return jwtUtil.extractUsernameFromRequest(request);
//     }

//     @Override
//     public boolean supportsParameter(MethodParameter parameter) {
//         if (parameter.getParameterAnnotation(CurrentUser.class) != null
//                 && parameter.getParameterType().equals(String.class)) {
//             return true;
//         }
//         return false;
//     }

// }
