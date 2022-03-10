package com.graduation.parrot.config.interceptor;

import com.graduation.parrot.config.auth.PrincipalDetails;
import com.graduation.parrot.domain.Board;
import com.graduation.parrot.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardAuthInterceptor implements HandlerInterceptor {

    private final BoardRepository boardRepository;

    /**
     * 게시글의 주인이 아닌 유저가 PUT, DELETE 요청을 하면 400 forbidden 에러를 보낸다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String httpMethod = request.getMethod();

        if (httpMethod.equals("PUT") || httpMethod.equals("DELETE")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
            Long sessionId = principal.getUser().getId();

            Map<?, ?> pathVariables = (Map<?, ?>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Long pathId = Long.parseLong((String) pathVariables.get("id"));

            Board board = boardRepository.findBoardById(pathId);
            Long user_id = board.getUser().getId();

            if (!sessionId.equals(user_id)) {
                response.setStatus(400);
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
