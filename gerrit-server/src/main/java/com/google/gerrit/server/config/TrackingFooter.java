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
DECL|package|com.google.gerrit.server.config
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|config
package|;
end_package

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|FooterKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|revwalk
operator|.
name|FooterLine
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|PatternSyntaxException
import|;
end_import

begin_comment
comment|/** Tracking entry in the configuration file */
end_comment

begin_class
DECL|class|TrackingFooter
specifier|public
class|class
name|TrackingFooter
block|{
DECL|field|key
specifier|private
specifier|final
name|FooterKey
name|key
decl_stmt|;
DECL|field|match
specifier|private
specifier|final
name|Pattern
name|match
decl_stmt|;
DECL|field|system
specifier|private
specifier|final
name|String
name|system
decl_stmt|;
DECL|method|TrackingFooter (String f, final String m, final String s)
specifier|public
name|TrackingFooter
parameter_list|(
name|String
name|f
parameter_list|,
specifier|final
name|String
name|m
parameter_list|,
specifier|final
name|String
name|s
parameter_list|)
throws|throws
name|PatternSyntaxException
block|{
name|f
operator|=
name|f
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|f
operator|.
name|endsWith
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|f
operator|=
name|f
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|f
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
name|key
operator|=
operator|new
name|FooterKey
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|this
operator|.
name|match
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|m
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|system
operator|=
name|s
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
comment|/** {@link FooterKey} to match in the commit message */
DECL|method|footerKey ()
specifier|public
name|FooterKey
name|footerKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/** Regex for parsing out external tracking id from {@link FooterLine} */
DECL|method|match ()
specifier|public
name|Pattern
name|match
parameter_list|()
block|{
return|return
name|match
return|;
block|}
comment|/** Name of the remote tracking system */
DECL|method|system ()
specifier|public
name|String
name|system
parameter_list|()
block|{
return|return
name|system
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
literal|"footer = "
operator|+
name|key
operator|.
name|getName
argument_list|()
operator|+
literal|", match = "
operator|+
name|match
operator|.
name|pattern
argument_list|()
operator|+
literal|", system = "
operator|+
name|system
return|;
block|}
block|}
end_class

end_unit

