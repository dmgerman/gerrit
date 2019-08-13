begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.rules
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|rules
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
name|common
operator|.
name|data
operator|.
name|SubmitRecord
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
name|common
operator|.
name|data
operator|.
name|SubmitTypeRecord
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
name|ProjectCache
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
name|ProjectState
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
name|google
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Singleton
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|PrologRule
specifier|public
class|class
name|PrologRule
implements|implements
name|SubmitRule
block|{
DECL|field|factory
specifier|private
specifier|final
name|PrologRuleEvaluator
operator|.
name|Factory
name|factory
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
annotation|@
name|Inject
DECL|method|PrologRule (PrologRuleEvaluator.Factory factory, ProjectCache projectCache)
specifier|private
name|PrologRule
parameter_list|(
name|PrologRuleEvaluator
operator|.
name|Factory
name|factory
parameter_list|,
name|ProjectCache
name|projectCache
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|evaluate (ChangeData cd)
specifier|public
name|Optional
argument_list|<
name|SubmitRecord
argument_list|>
name|evaluate
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
name|ProjectState
name|projectState
init|=
name|projectCache
operator|.
name|get
argument_list|(
name|cd
operator|.
name|project
argument_list|()
argument_list|)
decl_stmt|;
comment|// We only want to run the Prolog engine if we have at least one rules.pl file to use.
if|if
condition|(
operator|(
name|projectState
operator|==
literal|null
operator|||
operator|!
name|projectState
operator|.
name|hasPrologRules
argument_list|()
operator|)
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
return|return
name|Optional
operator|.
name|of
argument_list|(
name|evaluate
argument_list|(
name|cd
argument_list|,
name|PrologOptions
operator|.
name|defaultOptions
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|evaluate (ChangeData cd, PrologOptions opts)
specifier|public
name|SubmitRecord
name|evaluate
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|PrologOptions
name|opts
parameter_list|)
block|{
return|return
name|getEvaluator
argument_list|(
name|cd
argument_list|,
name|opts
argument_list|)
operator|.
name|evaluate
argument_list|()
return|;
block|}
DECL|method|getSubmitType (ChangeData cd)
specifier|public
name|SubmitTypeRecord
name|getSubmitType
parameter_list|(
name|ChangeData
name|cd
parameter_list|)
block|{
return|return
name|getSubmitType
argument_list|(
name|cd
argument_list|,
name|PrologOptions
operator|.
name|defaultOptions
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getSubmitType (ChangeData cd, PrologOptions opts)
specifier|public
name|SubmitTypeRecord
name|getSubmitType
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|PrologOptions
name|opts
parameter_list|)
block|{
return|return
name|getEvaluator
argument_list|(
name|cd
argument_list|,
name|opts
argument_list|)
operator|.
name|getSubmitType
argument_list|()
return|;
block|}
DECL|method|getEvaluator (ChangeData cd, PrologOptions opts)
specifier|private
name|PrologRuleEvaluator
name|getEvaluator
parameter_list|(
name|ChangeData
name|cd
parameter_list|,
name|PrologOptions
name|opts
parameter_list|)
block|{
return|return
name|factory
operator|.
name|create
argument_list|(
name|cd
argument_list|,
name|opts
argument_list|)
return|;
block|}
block|}
end_class

end_unit

