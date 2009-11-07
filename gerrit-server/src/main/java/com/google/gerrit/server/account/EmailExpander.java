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

begin_comment
comment|/**  * Expands user name to a local email address, usually by adding a domain.  */
end_comment

begin_interface
DECL|interface|EmailExpander
specifier|public
interface|interface
name|EmailExpander
block|{
DECL|method|canExpand (String user)
specifier|public
name|boolean
name|canExpand
parameter_list|(
name|String
name|user
parameter_list|)
function_decl|;
DECL|method|expand (String user)
specifier|public
name|String
name|expand
parameter_list|(
name|String
name|user
parameter_list|)
function_decl|;
DECL|class|None
specifier|public
specifier|static
class|class
name|None
implements|implements
name|EmailExpander
block|{
DECL|field|INSTANCE
specifier|public
specifier|static
specifier|final
name|None
name|INSTANCE
init|=
operator|new
name|None
argument_list|()
decl_stmt|;
DECL|method|canHandle (final String fmt)
specifier|public
specifier|static
name|boolean
name|canHandle
parameter_list|(
specifier|final
name|String
name|fmt
parameter_list|)
block|{
return|return
name|fmt
operator|==
literal|null
operator|||
name|fmt
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|None ()
specifier|private
name|None
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|canExpand (String user)
specifier|public
name|boolean
name|canExpand
parameter_list|(
name|String
name|user
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|expand (String user)
specifier|public
name|String
name|expand
parameter_list|(
name|String
name|user
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|class|Simple
specifier|public
specifier|static
class|class
name|Simple
implements|implements
name|EmailExpander
block|{
DECL|field|PLACEHOLDER
specifier|private
specifier|static
specifier|final
name|String
name|PLACEHOLDER
init|=
literal|"{0}"
decl_stmt|;
DECL|method|canHandle (final String fmt)
specifier|public
specifier|static
name|boolean
name|canHandle
parameter_list|(
specifier|final
name|String
name|fmt
parameter_list|)
block|{
return|return
name|fmt
operator|!=
literal|null
operator|&&
name|fmt
operator|.
name|contains
argument_list|(
name|PLACEHOLDER
argument_list|)
return|;
block|}
DECL|field|lhs
specifier|private
specifier|final
name|String
name|lhs
decl_stmt|;
DECL|field|rhs
specifier|private
specifier|final
name|String
name|rhs
decl_stmt|;
DECL|method|Simple (final String fmt)
specifier|public
name|Simple
parameter_list|(
specifier|final
name|String
name|fmt
parameter_list|)
block|{
specifier|final
name|int
name|p
init|=
name|fmt
operator|.
name|indexOf
argument_list|(
name|PLACEHOLDER
argument_list|)
decl_stmt|;
name|lhs
operator|=
name|fmt
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|p
argument_list|)
expr_stmt|;
name|rhs
operator|=
name|fmt
operator|.
name|substring
argument_list|(
name|p
operator|+
name|PLACEHOLDER
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|canExpand (final String user)
specifier|public
name|boolean
name|canExpand
parameter_list|(
specifier|final
name|String
name|user
parameter_list|)
block|{
return|return
name|user
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
operator|<
literal|0
return|;
block|}
DECL|method|expand (final String user)
specifier|public
name|String
name|expand
parameter_list|(
specifier|final
name|String
name|user
parameter_list|)
block|{
return|return
name|lhs
operator|+
name|user
operator|+
name|rhs
return|;
block|}
block|}
block|}
end_interface

end_unit

