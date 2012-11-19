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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
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
name|Account
import|;
end_import

begin_class
DECL|class|ReviewerJson
specifier|public
class|class
name|ReviewerJson
block|{
DECL|method|ReviewerJson ()
name|ReviewerJson
parameter_list|()
block|{   }
DECL|method|format (ReviewerResource reviewerResource)
specifier|public
name|ReviewerInfo
name|format
parameter_list|(
name|ReviewerResource
name|reviewerResource
parameter_list|)
block|{
name|ReviewerInfo
name|reviewerInfo
init|=
operator|new
name|ReviewerInfo
argument_list|()
decl_stmt|;
name|Account
name|account
init|=
name|reviewerResource
operator|.
name|getAccount
argument_list|()
decl_stmt|;
name|reviewerInfo
operator|.
name|id
operator|=
name|account
operator|.
name|getId
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
name|reviewerInfo
operator|.
name|email
operator|=
name|account
operator|.
name|getPreferredEmail
argument_list|()
expr_stmt|;
name|reviewerInfo
operator|.
name|name
operator|=
name|account
operator|.
name|getFullName
argument_list|()
expr_stmt|;
return|return
name|reviewerInfo
return|;
block|}
DECL|class|ReviewerInfo
specifier|public
specifier|static
class|class
name|ReviewerInfo
block|{
DECL|field|kind
specifier|final
name|String
name|kind
init|=
literal|"gerritcodereview#reviewer"
decl_stmt|;
DECL|field|id
name|String
name|id
decl_stmt|;
DECL|field|email
name|String
name|email
decl_stmt|;
DECL|field|name
name|String
name|name
decl_stmt|;
block|}
block|}
end_class

end_unit

