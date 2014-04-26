begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.server.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|project
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkNotNull
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Lists
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|Change
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|client
operator|.
name|PatchSet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|reviewdb
operator|.
name|server
operator|.
name|ReviewDb
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|rules
operator|.
name|PrologEnvironment
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|rules
operator|.
name|StoredValues
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|query
operator|.
name|change
operator|.
name|ChangeData
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|compiler
operator|.
name|CompileException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|ListTerm
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Prolog
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|PrologException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|Term
import|;
end_import

begin_import
import|import
name|com
operator|.
name|googlecode
operator|.
name|prolog_cafe
operator|.
name|lang
operator|.
name|VariableTerm
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Evaluates a submit-like Prolog rule found in the rules.pl file of the current  * project and filters the results through rules found in the parent projects,  * all the way up to All-Projects.  */
end_comment

begin_class
DECL|class|SubmitRuleEvaluator
specifier|public
class|class
name|SubmitRuleEvaluator
block|{
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|patchSet
specifier|private
specifier|final
name|PatchSet
name|patchSet
decl_stmt|;
DECL|field|projectControl
specifier|private
specifier|final
name|ProjectControl
name|projectControl
decl_stmt|;
DECL|field|changeControl
specifier|private
specifier|final
name|ChangeControl
name|changeControl
decl_stmt|;
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|field|cd
specifier|private
specifier|final
name|ChangeData
name|cd
decl_stmt|;
DECL|field|fastEvalLabels
specifier|private
specifier|final
name|boolean
name|fastEvalLabels
decl_stmt|;
DECL|field|userRuleLocatorName
specifier|private
specifier|final
name|String
name|userRuleLocatorName
decl_stmt|;
DECL|field|userRuleWrapperName
specifier|private
specifier|final
name|String
name|userRuleWrapperName
decl_stmt|;
DECL|field|filterRuleLocatorName
specifier|private
specifier|final
name|String
name|filterRuleLocatorName
decl_stmt|;
DECL|field|filterRuleWrapperName
specifier|private
specifier|final
name|String
name|filterRuleWrapperName
decl_stmt|;
DECL|field|skipFilters
specifier|private
specifier|final
name|boolean
name|skipFilters
decl_stmt|;
DECL|field|rulesInputStream
specifier|private
specifier|final
name|InputStream
name|rulesInputStream
decl_stmt|;
DECL|field|submitRule
specifier|private
name|Term
name|submitRule
decl_stmt|;
DECL|field|projectName
specifier|private
name|String
name|projectName
decl_stmt|;
comment|/**    * @param userRuleLocatorName The name of the rule used to locate the    *        user-supplied rule.    * @param userRuleWrapperName The name of the wrapper rule used to evaluate    *        the user-supplied rule.    * @param filterRuleLocatorName The name of the rule used to locate the filter    *        rule.    * @param filterRuleWrapperName The name of the rule used to evaluate the    *        filter rule.    */
DECL|method|SubmitRuleEvaluator (ReviewDb db, PatchSet patchSet, ProjectControl projectControl, ChangeControl changeControl, Change change, ChangeData cd, boolean fastEvalLabels, String userRuleLocatorName, String userRuleWrapperName, String filterRuleLocatorName, String filterRuleWrapperName)
specifier|public
name|SubmitRuleEvaluator
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|PatchSet
name|patchSet
parameter_list|,
name|ProjectControl
name|projectControl
parameter_list|,
name|ChangeControl
name|changeControl
parameter_list|,
name|Change
name|change
parameter_list|,
name|ChangeData
name|cd
parameter_list|,
name|boolean
name|fastEvalLabels
parameter_list|,
name|String
name|userRuleLocatorName
parameter_list|,
name|String
name|userRuleWrapperName
parameter_list|,
name|String
name|filterRuleLocatorName
parameter_list|,
name|String
name|filterRuleWrapperName
parameter_list|)
block|{
name|this
argument_list|(
name|db
argument_list|,
name|patchSet
argument_list|,
name|projectControl
argument_list|,
name|changeControl
argument_list|,
name|change
argument_list|,
name|cd
argument_list|,
name|fastEvalLabels
argument_list|,
name|userRuleLocatorName
argument_list|,
name|userRuleWrapperName
argument_list|,
name|filterRuleLocatorName
argument_list|,
name|filterRuleWrapperName
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|/**    * @param userRuleLocatorName The name of the rule used to locate the    *        user-supplied rule.    * @param userRuleWrapperName The name of the wrapper rule used to evaluate    *        the user-supplied rule.    * @param filterRuleLocatorName The name of the rule used to locate the filter    *        rule.    * @param filterRuleWrapperName The name of the rule used to evaluate the    *        filter rule.    * @param skipSubmitFilters if {@code true} submit filter will not be    *        applied    * @param rules when non-null the rules will be read from this input stream    *        instead of refs/meta/config:rules.pl file    */
DECL|method|SubmitRuleEvaluator (ReviewDb db, PatchSet patchSet, ProjectControl projectControl, ChangeControl changeControl, Change change, ChangeData cd, boolean fastEvalLabels, String userRuleLocatorName, String userRuleWrapperName, String filterRuleLocatorName, String filterRuleWrapperName, boolean skipSubmitFilters, InputStream rules)
specifier|public
name|SubmitRuleEvaluator
parameter_list|(
name|ReviewDb
name|db
parameter_list|,
name|PatchSet
name|patchSet
parameter_list|,
name|ProjectControl
name|projectControl
parameter_list|,
name|ChangeControl
name|changeControl
parameter_list|,
name|Change
name|change
parameter_list|,
name|ChangeData
name|cd
parameter_list|,
name|boolean
name|fastEvalLabels
parameter_list|,
name|String
name|userRuleLocatorName
parameter_list|,
name|String
name|userRuleWrapperName
parameter_list|,
name|String
name|filterRuleLocatorName
parameter_list|,
name|String
name|filterRuleWrapperName
parameter_list|,
name|boolean
name|skipSubmitFilters
parameter_list|,
name|InputStream
name|rules
parameter_list|)
block|{
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|patchSet
operator|=
name|patchSet
expr_stmt|;
name|this
operator|.
name|projectControl
operator|=
name|projectControl
expr_stmt|;
name|this
operator|.
name|changeControl
operator|=
name|changeControl
expr_stmt|;
name|this
operator|.
name|change
operator|=
name|change
expr_stmt|;
name|this
operator|.
name|cd
operator|=
name|checkNotNull
argument_list|(
name|cd
argument_list|,
literal|"ChangeData"
argument_list|)
expr_stmt|;
name|this
operator|.
name|fastEvalLabels
operator|=
name|fastEvalLabels
expr_stmt|;
name|this
operator|.
name|userRuleLocatorName
operator|=
name|userRuleLocatorName
expr_stmt|;
name|this
operator|.
name|userRuleWrapperName
operator|=
name|userRuleWrapperName
expr_stmt|;
name|this
operator|.
name|filterRuleLocatorName
operator|=
name|filterRuleLocatorName
expr_stmt|;
name|this
operator|.
name|filterRuleWrapperName
operator|=
name|filterRuleWrapperName
expr_stmt|;
name|this
operator|.
name|skipFilters
operator|=
name|skipSubmitFilters
expr_stmt|;
name|this
operator|.
name|rulesInputStream
operator|=
name|rules
expr_stmt|;
block|}
comment|/**    * Evaluates the given rule and filters.    *    * Sets the {@link #submitRule} to the Term found by the    * {@link #userRuleLocatorName}. This can be used when reporting error(s) on    * unexpected return value of this method.    *    * @return List of {@link Term} objects returned from the evaluated rules.    * @throws RuleEvalException    */
DECL|method|evaluate ()
specifier|public
name|List
argument_list|<
name|Term
argument_list|>
name|evaluate
parameter_list|()
throws|throws
name|RuleEvalException
block|{
name|PrologEnvironment
name|env
init|=
name|getPrologEnvironment
argument_list|()
decl_stmt|;
try|try
block|{
name|submitRule
operator|=
name|env
operator|.
name|once
argument_list|(
literal|"gerrit"
argument_list|,
name|userRuleLocatorName
argument_list|,
operator|new
name|VariableTerm
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|fastEvalLabels
condition|)
block|{
name|env
operator|.
name|once
argument_list|(
literal|"gerrit"
argument_list|,
literal|"assume_range_from_label"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Term
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
for|for
control|(
name|Term
index|[]
name|template
range|:
name|env
operator|.
name|all
argument_list|(
literal|"gerrit"
argument_list|,
name|userRuleWrapperName
argument_list|,
name|submitRule
argument_list|,
operator|new
name|VariableTerm
argument_list|()
argument_list|)
control|)
block|{
name|results
operator|.
name|add
argument_list|(
name|template
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|PrologException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|RuleEvalException
argument_list|(
literal|"Exception calling "
operator|+
name|submitRule
operator|+
literal|" on change "
operator|+
name|change
operator|.
name|getId
argument_list|()
operator|+
literal|" of "
operator|+
name|getProjectName
argument_list|()
argument_list|,
name|err
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|RuleEvalException
argument_list|(
literal|"Exception calling "
operator|+
name|submitRule
operator|+
literal|" on change "
operator|+
name|change
operator|.
name|getId
argument_list|()
operator|+
literal|" of "
operator|+
name|getProjectName
argument_list|()
argument_list|,
name|err
argument_list|)
throw|;
block|}
name|Term
name|resultsTerm
init|=
name|toListTerm
argument_list|(
name|results
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|skipFilters
condition|)
block|{
name|resultsTerm
operator|=
name|runSubmitFilters
argument_list|(
name|resultsTerm
argument_list|,
name|env
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resultsTerm
operator|.
name|isList
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Term
argument_list|>
name|r
init|=
name|Lists
operator|.
name|newArrayList
argument_list|()
decl_stmt|;
for|for
control|(
name|Term
name|t
init|=
name|resultsTerm
init|;
name|t
operator|.
name|isList
argument_list|()
condition|;
control|)
block|{
name|ListTerm
name|l
init|=
operator|(
name|ListTerm
operator|)
name|t
decl_stmt|;
name|r
operator|.
name|add
argument_list|(
name|l
operator|.
name|car
argument_list|()
operator|.
name|dereference
argument_list|()
argument_list|)
expr_stmt|;
name|t
operator|=
name|l
operator|.
name|cdr
argument_list|()
operator|.
name|dereference
argument_list|()
expr_stmt|;
block|}
return|return
name|r
return|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
finally|finally
block|{
name|env
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getPrologEnvironment ()
specifier|private
name|PrologEnvironment
name|getPrologEnvironment
parameter_list|()
throws|throws
name|RuleEvalException
block|{
name|ProjectState
name|projectState
init|=
name|projectControl
operator|.
name|getProjectState
argument_list|()
decl_stmt|;
name|PrologEnvironment
name|env
decl_stmt|;
try|try
block|{
if|if
condition|(
name|rulesInputStream
operator|==
literal|null
condition|)
block|{
name|env
operator|=
name|projectState
operator|.
name|newPrologEnvironment
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|env
operator|=
name|projectState
operator|.
name|newPrologEnvironment
argument_list|(
literal|"stdin"
argument_list|,
name|rulesInputStream
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|CompileException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|RuleEvalException
argument_list|(
literal|"Cannot consult rules.pl for "
operator|+
name|getProjectName
argument_list|()
argument_list|,
name|err
argument_list|)
throw|;
block|}
name|env
operator|.
name|set
argument_list|(
name|StoredValues
operator|.
name|REVIEW_DB
argument_list|,
name|db
argument_list|)
expr_stmt|;
name|env
operator|.
name|set
argument_list|(
name|StoredValues
operator|.
name|CHANGE_DATA
argument_list|,
name|cd
argument_list|)
expr_stmt|;
name|env
operator|.
name|set
argument_list|(
name|StoredValues
operator|.
name|PATCH_SET
argument_list|,
name|patchSet
argument_list|)
expr_stmt|;
name|env
operator|.
name|set
argument_list|(
name|StoredValues
operator|.
name|CHANGE_CONTROL
argument_list|,
name|changeControl
argument_list|)
expr_stmt|;
return|return
name|env
return|;
block|}
DECL|method|runSubmitFilters (Term results, PrologEnvironment env)
specifier|private
name|Term
name|runSubmitFilters
parameter_list|(
name|Term
name|results
parameter_list|,
name|PrologEnvironment
name|env
parameter_list|)
throws|throws
name|RuleEvalException
block|{
name|ProjectState
name|projectState
init|=
name|projectControl
operator|.
name|getProjectState
argument_list|()
decl_stmt|;
name|PrologEnvironment
name|childEnv
init|=
name|env
decl_stmt|;
for|for
control|(
name|ProjectState
name|parentState
range|:
name|projectState
operator|.
name|parents
argument_list|()
control|)
block|{
name|PrologEnvironment
name|parentEnv
decl_stmt|;
try|try
block|{
name|parentEnv
operator|=
name|parentState
operator|.
name|newPrologEnvironment
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CompileException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|RuleEvalException
argument_list|(
literal|"Cannot consult rules.pl for "
operator|+
name|parentState
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|err
argument_list|)
throw|;
block|}
name|parentEnv
operator|.
name|copyStoredValues
argument_list|(
name|childEnv
argument_list|)
expr_stmt|;
name|Term
name|filterRule
init|=
name|parentEnv
operator|.
name|once
argument_list|(
literal|"gerrit"
argument_list|,
name|filterRuleLocatorName
argument_list|,
operator|new
name|VariableTerm
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|fastEvalLabels
condition|)
block|{
name|env
operator|.
name|once
argument_list|(
literal|"gerrit"
argument_list|,
literal|"assume_range_from_label"
argument_list|)
expr_stmt|;
block|}
name|Term
index|[]
name|template
init|=
name|parentEnv
operator|.
name|once
argument_list|(
literal|"gerrit"
argument_list|,
name|filterRuleWrapperName
argument_list|,
name|filterRule
argument_list|,
name|results
argument_list|,
operator|new
name|VariableTerm
argument_list|()
argument_list|)
decl_stmt|;
name|results
operator|=
name|template
index|[
literal|2
index|]
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PrologException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|RuleEvalException
argument_list|(
literal|"Exception calling "
operator|+
name|filterRule
operator|+
literal|" on change "
operator|+
name|change
operator|.
name|getId
argument_list|()
operator|+
literal|" of "
operator|+
name|parentState
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|err
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|err
parameter_list|)
block|{
throw|throw
operator|new
name|RuleEvalException
argument_list|(
literal|"Exception calling "
operator|+
name|filterRule
operator|+
literal|" on change "
operator|+
name|change
operator|.
name|getId
argument_list|()
operator|+
literal|" of "
operator|+
name|parentState
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|err
argument_list|)
throw|;
block|}
name|childEnv
operator|=
name|parentEnv
expr_stmt|;
block|}
return|return
name|results
return|;
block|}
DECL|method|toListTerm (List<Term> terms)
specifier|private
specifier|static
name|Term
name|toListTerm
parameter_list|(
name|List
argument_list|<
name|Term
argument_list|>
name|terms
parameter_list|)
block|{
name|Term
name|list
init|=
name|Prolog
operator|.
name|Nil
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|terms
operator|.
name|size
argument_list|()
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|list
operator|=
operator|new
name|ListTerm
argument_list|(
name|terms
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
DECL|method|getSubmitRule ()
specifier|public
name|Term
name|getSubmitRule
parameter_list|()
block|{
return|return
name|submitRule
return|;
block|}
DECL|method|getProjectName ()
specifier|private
name|String
name|getProjectName
parameter_list|()
block|{
if|if
condition|(
name|projectName
operator|==
literal|null
condition|)
block|{
name|projectName
operator|=
name|projectControl
operator|.
name|getProjectState
argument_list|()
operator|.
name|getProject
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
return|return
name|projectName
return|;
block|}
block|}
end_class

end_unit

