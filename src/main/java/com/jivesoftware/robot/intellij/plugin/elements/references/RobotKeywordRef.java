package com.jivesoftware.robot.intellij.plugin.elements.references;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.jivesoftware.robot.intellij.plugin.elements.RobotPsiUtil;
import com.jivesoftware.robot.intellij.plugin.psi.RobotKeyword;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RobotKeywordRef extends PsiReferenceBase<PsiElement> {

  public RobotKeywordRef(PsiElement element) {
    super(element);
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    Project project = myElement.getProject();

    RobotKeywordDefinitionFinder robotKeywordDefinitionFinder = new RobotKeywordDefinitionFinder(project, getCanonicalText(), RobotKeywordDefinitionFinder.KEYWORD_SCOPE.ROBOT_AND_JAVA_KEYWORDS, false);
    robotKeywordDefinitionFinder.process();
    List<PsiElement> results = robotKeywordDefinitionFinder.getResults();
    if (results.size() <= 0) {
      return null;
    }
    return results.get(0);
  }

  /**
   * Return the java method name corresponding to the Robot keyword.
   */
  @NotNull
  @Override
  public String getCanonicalText() {
    String keywordText = myElement.getText();
    return keywordText;
  }

  @NotNull
  @Override
  public Object[] getVariants() {
    return new Object[0]; // code completion handled by RobotCompletionProvider
  }

  @Override
  public TextRange calculateDefaultRangeInElement() {
    return new TextRange(0, myElement.getText().length());
  }

  @Override
  public PsiElement handleElementRename(String name) {
      if (myElement instanceof RobotKeyword) {
          return ((RobotKeyword)myElement).setName(RobotPsiUtil.methodToRobotKeyword(name));
      }
      return myElement;
  }

}
