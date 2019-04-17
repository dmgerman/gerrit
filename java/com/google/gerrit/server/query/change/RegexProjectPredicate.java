begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.query.change
package|package
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
name|Project
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
name|index
operator|.
name|change
operator|.
name|ChangeField
import|;
end_import

begin_import
import|import
name|dk
operator|.
name|brics
operator|.
name|automaton
operator|.
name|RegExp
import|;
end_import

begin_import
import|import
name|dk
operator|.
name|brics
operator|.
name|automaton
operator|.
name|RunAutomaton
import|;
end_import

begin_class
DECL|class|RegexProjectPredicate
specifier|public
class|class
name|RegexProjectPredicate
extends|extends
name|ChangeRegexPredicate
block|{
DECL|field|pattern
specifier|protected
specifier|final
name|RunAutomaton
name|pattern
decl_stmt|;
DECL|method|RegexProjectPredicate (String re)
specifier|public
name|RegexProjectPredicate
parameter_list|(
name|String
name|re
parameter_list|)
block|{
name|super
argument_list|(
name|ChangeField
operator|.
name|PROJECT
argument_list|,
name|re
argument_list|)
expr_stmt|;
if|if
condition|(
name|re
operator|.
name|startsWith
argument_list|(
literal|"^"
argument_list|)
condition|)
block|{
name|re
operator|=
name|re
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|re
operator|.
name|endsWith
argument_list|(
literal|"$"
argument_list|)
operator|&&
operator|!
name|re
operator|.
name|endsWith
argument_list|(
literal|"\\$"
argument_list|)
condition|)
block|{
name|re
operator|=
name|re
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|re
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|pattern
operator|=
operator|new
name|RunAutomaton
argument_list|(
operator|new
name|RegExp
argument_list|(
name|re
argument_list|)
operator|.
name|toAutomaton
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|match (ChangeData object)
specifier|public
name|boolean
name|match
parameter_list|(
name|ChangeData
name|object
parameter_list|)
block|{
name|Change
name|change
init|=
name|object
operator|.
name|change
argument_list|()
decl_stmt|;
if|if
condition|(
name|change
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
name|Project
operator|.
name|NameKey
name|p
init|=
name|change
operator|.
name|getDest
argument_list|()
operator|.
name|project
argument_list|()
decl_stmt|;
return|return
name|pattern
operator|.
name|run
argument_list|(
name|p
operator|.
name|get
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getCost ()
specifier|public
name|int
name|getCost
parameter_list|()
block|{
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

