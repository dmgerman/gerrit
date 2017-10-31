begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.api.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|api
operator|.
name|config
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_class
DECL|class|ConsistencyCheckInfo
specifier|public
class|class
name|ConsistencyCheckInfo
block|{
DECL|field|checkAccountsResult
specifier|public
name|CheckAccountsResultInfo
name|checkAccountsResult
decl_stmt|;
DECL|field|checkAccountExternalIdsResult
specifier|public
name|CheckAccountExternalIdsResultInfo
name|checkAccountExternalIdsResult
decl_stmt|;
DECL|class|CheckAccountsResultInfo
specifier|public
specifier|static
class|class
name|CheckAccountsResultInfo
block|{
DECL|field|problems
specifier|public
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
decl_stmt|;
DECL|method|CheckAccountsResultInfo (List<ConsistencyProblemInfo> problems)
specifier|public
name|CheckAccountsResultInfo
parameter_list|(
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
parameter_list|)
block|{
name|this
operator|.
name|problems
operator|=
name|problems
expr_stmt|;
block|}
block|}
DECL|class|CheckAccountExternalIdsResultInfo
specifier|public
specifier|static
class|class
name|CheckAccountExternalIdsResultInfo
block|{
DECL|field|problems
specifier|public
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
decl_stmt|;
DECL|method|CheckAccountExternalIdsResultInfo (List<ConsistencyProblemInfo> problems)
specifier|public
name|CheckAccountExternalIdsResultInfo
parameter_list|(
name|List
argument_list|<
name|ConsistencyProblemInfo
argument_list|>
name|problems
parameter_list|)
block|{
name|this
operator|.
name|problems
operator|=
name|problems
expr_stmt|;
block|}
block|}
DECL|class|ConsistencyProblemInfo
specifier|public
specifier|static
class|class
name|ConsistencyProblemInfo
block|{
DECL|enum|Status
specifier|public
enum|enum
name|Status
block|{
DECL|enumConstant|ERROR
name|ERROR
block|,
DECL|enumConstant|WARNING
name|WARNING
block|,     }
DECL|field|status
specifier|public
specifier|final
name|Status
name|status
decl_stmt|;
DECL|field|message
specifier|public
specifier|final
name|String
name|message
decl_stmt|;
DECL|method|ConsistencyProblemInfo (Status status, String message)
specifier|public
name|ConsistencyProblemInfo
parameter_list|(
name|Status
name|status
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|o
operator|instanceof
name|ConsistencyProblemInfo
condition|)
block|{
name|ConsistencyProblemInfo
name|other
init|=
operator|(
operator|(
name|ConsistencyProblemInfo
operator|)
name|o
operator|)
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|status
argument_list|,
name|other
operator|.
name|status
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|message
argument_list|,
name|other
operator|.
name|message
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|Objects
operator|.
name|hash
argument_list|(
name|status
argument_list|,
name|message
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|status
operator|.
name|name
argument_list|()
operator|+
literal|": "
operator|+
name|message
return|;
block|}
block|}
block|}
end_class

end_unit

