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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
package|;
end_package

begin_comment
comment|/**  * Wrapper for {@link org.eclipse.jgit.errors.LargeObjectException}. Since  * org.eclipse.jgit.errors.LargeObjectException is a {@link RuntimeException}  * the GerritJsonServlet would treat it as internal failure and as result the  * web ui would just show 'Internal Server Error'. Wrapping  * org.eclipse.jgit.errors.LargeObjectException into a normal {@link Exception}  * allows to display a proper error message.  */
end_comment

begin_class
DECL|class|LargeObjectException
specifier|public
class|class
name|LargeObjectException
extends|extends
name|Exception
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|method|LargeObjectException (final String message, final org.eclipse.jgit.errors.LargeObjectException cause)
specifier|public
name|LargeObjectException
parameter_list|(
specifier|final
name|String
name|message
parameter_list|,
specifier|final
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|errors
operator|.
name|LargeObjectException
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

