begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
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
name|errors
operator|.
name|NotSignedInException
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
name|CurrentUser
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
name|IdentifiedUser
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
name|Provider
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
name|ProvisionException
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
name|servlet
operator|.
name|RequestScoped
import|;
end_import

begin_class
annotation|@
name|RequestScoped
DECL|class|HttpIdentifiedUserProvider
class|class
name|HttpIdentifiedUserProvider
implements|implements
name|Provider
argument_list|<
name|IdentifiedUser
argument_list|>
block|{
DECL|field|user
specifier|private
specifier|final
name|CurrentUser
name|user
decl_stmt|;
annotation|@
name|Inject
DECL|method|HttpIdentifiedUserProvider (final CurrentUser u)
name|HttpIdentifiedUserProvider
parameter_list|(
specifier|final
name|CurrentUser
name|u
parameter_list|)
block|{
name|user
operator|=
name|u
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|IdentifiedUser
name|get
parameter_list|()
block|{
if|if
condition|(
name|user
operator|instanceof
name|IdentifiedUser
condition|)
block|{
return|return
operator|(
name|IdentifiedUser
operator|)
name|user
return|;
block|}
throw|throw
operator|new
name|ProvisionException
argument_list|(
name|NotSignedInException
operator|.
name|MESSAGE
argument_list|,
operator|new
name|NotSignedInException
argument_list|()
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

