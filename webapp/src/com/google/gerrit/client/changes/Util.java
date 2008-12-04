begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright 2008 Google Inc.
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
DECL|package|com.google.gerrit.client.changes
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|changes
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
name|client
operator|.
name|reviewdb
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
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|GWT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtjsonrpc
operator|.
name|client
operator|.
name|JsonUtil
import|;
end_import

begin_class
DECL|class|Util
specifier|public
class|class
name|Util
block|{
DECL|field|C
specifier|public
specifier|static
specifier|final
name|ChangeConstants
name|C
init|=
name|GWT
operator|.
name|create
argument_list|(
name|ChangeConstants
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|M
specifier|public
specifier|static
specifier|final
name|ChangeMessages
name|M
init|=
name|GWT
operator|.
name|create
argument_list|(
name|ChangeMessages
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DETAIL_SVC
specifier|public
specifier|static
specifier|final
name|ChangeDetailService
name|DETAIL_SVC
decl_stmt|;
DECL|field|LIST_SVC
specifier|public
specifier|static
specifier|final
name|ChangeListService
name|LIST_SVC
decl_stmt|;
static|static
block|{
name|DETAIL_SVC
operator|=
name|GWT
operator|.
name|create
argument_list|(
name|ChangeDetailService
operator|.
name|class
argument_list|)
expr_stmt|;
name|JsonUtil
operator|.
name|bind
argument_list|(
name|DETAIL_SVC
argument_list|,
literal|"rpc/ChangeDetailService"
argument_list|)
expr_stmt|;
name|LIST_SVC
operator|=
name|GWT
operator|.
name|create
argument_list|(
name|ChangeListService
operator|.
name|class
argument_list|)
expr_stmt|;
name|JsonUtil
operator|.
name|bind
argument_list|(
name|LIST_SVC
argument_list|,
literal|"rpc/ChangeListService"
argument_list|)
expr_stmt|;
block|}
DECL|method|toLongString (final Change.Status status)
specifier|public
specifier|static
name|String
name|toLongString
parameter_list|(
specifier|final
name|Change
operator|.
name|Status
name|status
parameter_list|)
block|{
if|if
condition|(
name|status
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
elseif|else
if|if
condition|(
name|status
operator|==
name|Change
operator|.
name|Status
operator|.
name|NEW
condition|)
block|{
return|return
name|C
operator|.
name|statusLongNew
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|status
operator|==
name|Change
operator|.
name|Status
operator|.
name|MERGED
condition|)
block|{
return|return
name|C
operator|.
name|statusLongMerged
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|status
operator|==
name|Change
operator|.
name|Status
operator|.
name|ABANDONED
condition|)
block|{
return|return
name|C
operator|.
name|statusLongAbandoned
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|status
operator|.
name|name
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

