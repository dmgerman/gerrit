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
DECL|package|com.google.gerrit.server.restapi.account
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|restapi
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
name|Response
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
name|gerrit
operator|.
name|server
operator|.
name|account
operator|.
name|AccountResource
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

begin_class
annotation|@
name|Singleton
DECL|class|GetEmail
specifier|public
class|class
name|GetEmail
implements|implements
name|RestReadView
argument_list|<
name|AccountResource
operator|.
name|Email
argument_list|>
block|{
annotation|@
name|Inject
DECL|method|GetEmail ()
specifier|public
name|GetEmail
parameter_list|()
block|{}
annotation|@
name|Override
DECL|method|apply (AccountResource.Email rsrc)
specifier|public
name|Response
argument_list|<
name|EmailInfo
argument_list|>
name|apply
parameter_list|(
name|AccountResource
operator|.
name|Email
name|rsrc
parameter_list|)
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
name|rsrc
operator|.
name|getEmail
argument_list|()
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
name|preferredEmail
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|e
argument_list|)
return|;
block|}
block|}
end_class

end_unit

