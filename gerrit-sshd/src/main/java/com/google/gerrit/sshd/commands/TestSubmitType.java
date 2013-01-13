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

begin_comment
comment|//
end_comment

begin_package
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
package|;
end_package

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
name|server
operator|.
name|project
operator|.
name|ChangeControl
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
name|project
operator|.
name|SubmitRuleEvaluator
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
name|sshd
operator|.
name|CommandMetaData
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
name|Term
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

begin_class
annotation|@
name|CommandMetaData
argument_list|(
name|name
operator|=
literal|"type"
argument_list|,
name|descr
operator|=
literal|"Test prolog submit type"
argument_list|)
DECL|class|TestSubmitType
specifier|final
class|class
name|TestSubmitType
extends|extends
name|BaseTestSubmit
block|{
annotation|@
name|Override
DECL|method|createEvaluator (PatchSet ps)
specifier|protected
name|SubmitRuleEvaluator
name|createEvaluator
parameter_list|(
name|PatchSet
name|ps
parameter_list|)
throws|throws
name|Exception
block|{
name|ChangeControl
name|cc
init|=
name|getChangeControl
argument_list|()
decl_stmt|;
return|return
operator|new
name|SubmitRuleEvaluator
argument_list|(
name|db
argument_list|,
name|ps
argument_list|,
name|cc
operator|.
name|getProjectControl
argument_list|()
argument_list|,
name|cc
argument_list|,
name|getChange
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|"locate_submit_type"
argument_list|,
literal|"get_submit_type"
argument_list|,
literal|"locate_submit_type_filter"
argument_list|,
literal|"filter_submit_type_results"
argument_list|,
name|skipSubmitFilters
argument_list|,
name|useStdin
condition|?
name|in
else|:
literal|null
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|processResults (ListTerm resultsTerm, Term submitRule)
specifier|protected
name|void
name|processResults
parameter_list|(
name|ListTerm
name|resultsTerm
parameter_list|,
name|Term
name|submitRule
parameter_list|)
throws|throws
name|Exception
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|results
init|=
name|resultsTerm
operator|.
name|toJava
argument_list|()
decl_stmt|;
if|if
condition|(
name|results
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Should never occur for a well written rule
name|Change
name|c
init|=
name|getChange
argument_list|()
decl_stmt|;
name|stderr
operator|.
name|print
argument_list|(
literal|"Submit rule "
operator|+
name|submitRule
operator|+
literal|" for change "
operator|+
name|c
operator|.
name|getChangeId
argument_list|()
operator|+
literal|" of "
operator|+
name|c
operator|.
name|getProject
argument_list|()
operator|.
name|get
argument_list|()
operator|+
literal|" has no solution"
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
name|typeName
init|=
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|stdout
operator|.
name|print
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
name|stdout
operator|.
name|print
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

