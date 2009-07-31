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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

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
name|SessionScoped
import|;
end_import

begin_comment
comment|/**  * Information about the currently logged in user.  *<p>  * This is a {@link RequestScoped} or {@link SessionScoped} property managed by  * Guice, depending on the environment. Application code should just assume it  * is {@code RequestScoped}.  *  * @see IdentifiedUser  */
end_comment

begin_class
DECL|class|CurrentUser
specifier|public
specifier|abstract
class|class
name|CurrentUser
block|{
comment|/** An anonymous user, the identity is not known. */
DECL|field|ANONYMOUS
specifier|public
specifier|static
specifier|final
name|CurrentUser
name|ANONYMOUS
init|=
operator|new
name|Anonymous
argument_list|()
decl_stmt|;
DECL|class|Anonymous
specifier|private
specifier|static
class|class
name|Anonymous
extends|extends
name|CurrentUser
block|{
DECL|method|Anonymous ()
specifier|private
name|Anonymous
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ANONYMOUS"
return|;
block|}
block|}
block|}
end_class

end_unit

