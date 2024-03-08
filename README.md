# Todo-List

# Version

TBA

# Class Diagram

TBA

# Git Convention

| message  | description    |
|----------|----------------|
| feat     | 새로운 기능 추가      |
| fix      | 문제 해결 또는 오류 수정 |
| rename   | 파일/폴더 이름 변경    |
| refactor | 코드 리팩토링        |
| chore    | 빌드, 패키지 매니징 변경 |
| test     | 테스트 코드 추가, 변경 |

# review

## 패키지 구조

```
root
+-- global
|   +-- config
|   +-- exception
|   `-- util
`-- domain
    +-- todo
    |   +-- controller
    |       ...
    `-- member
        ...
```

## DTO

`class`로 만들면 필요없는 코드가 늘어난다. `record`로 만들면 필요없는 코드를 줄일 수 있다.

민감한 Entity를 응답해야할 경우에는 보안을 위해 별도의 Dto를 만들어 필요한 값만 담아 응답한다.
```java
public record LoginResponse (
  // ...  
) {
  public static LoginResponse.of(Member member) {
    return new LoginResponse(member.getId(), /* ... */);
  }
}
```

## Builder

Entity에 setter를 만들면 entity가 수정된 이유에 대해 알기 힘들다. setter를 만들지 말고 항상 `final`로 객체를 만들되 builder를 사용하자. 
