begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|account
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
name|extensions
operator|.
name|common
operator|.
name|EmailInfo
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
name|extensions
operator|.
name|restapi
operator|.
name|RestReadView
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
name|Comparator
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
name|Singleton
DECL|class|GetEmails
specifier|public
class|class
name|GetEmails
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
argument_list|>
block|{
annotation|@
name|Override
DECL|method|apply (AccountResource rsrc)
specifier|public
name|List
argument_list|<
name|EmailInfo
argument_list|>
name|apply
parameter_list|(
name|AccountResource
name|rsrc
parameter_list|)
block|{
name|List
argument_list|<
name|EmailInfo
argument_list|>
name|emails
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|email
range|:
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|getEmailAddresses
argument_list|()
control|)
block|{
if|if
condition|(
name|email
operator|!=
literal|null
condition|)
block|{
name|EmailInfo
name|e
init|=
operator|new
name|EmailInfo
argument_list|()
decl_stmt|;
name|e
operator|.
name|email
operator|=
name|email
expr_stmt|;
name|e
operator|.
name|preferred
argument_list|(
name|rsrc
operator|.
name|getUser
argument_list|()
operator|.
name|getAccount
argument_list|()
operator|.
name|getPreferredEmail
argument_list|()
argument_list|)
expr_stmt|;
name|emails
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|emails
argument_list|,
operator|new
name|Comparator
argument_list|<
name|EmailInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
name|compare
parameter_list|(
name|EmailInfo
name|a
parameter_list|,
name|EmailInfo
name|b
parameter_list|)
block|{
return|return
name|a
operator|.
name|email
operator|.
name|compareTo
argument_list|(
name|b
operator|.
name|email
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|emails
return|;
block|}
block|}
end_class

end_unit

