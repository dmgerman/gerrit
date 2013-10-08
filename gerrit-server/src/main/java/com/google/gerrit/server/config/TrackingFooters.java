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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Sets
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Matcher
import|;
end_import

begin_class
DECL|class|TrackingFooters
specifier|public
class|class
name|TrackingFooters
block|{
DECL|field|trackingFooters
specifier|protected
name|List
argument_list|<
name|TrackingFooter
argument_list|>
name|trackingFooters
decl_stmt|;
DECL|method|TrackingFooters (final List<TrackingFooter> trFooters)
specifier|public
name|TrackingFooters
parameter_list|(
specifier|final
name|List
argument_list|<
name|TrackingFooter
argument_list|>
name|trFooters
parameter_list|)
block|{
name|trackingFooters
operator|=
name|trFooters
expr_stmt|;
block|}
DECL|method|getTrackingFooters ()
specifier|public
name|List
argument_list|<
name|TrackingFooter
argument_list|>
name|getTrackingFooters
parameter_list|()
block|{
return|return
name|trackingFooters
return|;
block|}
DECL|method|extract (List<FooterLine> lines)
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|extract
parameter_list|(
name|List
argument_list|<
name|FooterLine
argument_list|>
name|lines
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|r
init|=
name|Sets
operator|.
name|newHashSet
argument_list|()
decl_stmt|;
for|for
control|(
name|FooterLine
name|footer
range|:
name|lines
control|)
block|{
for|for
control|(
name|TrackingFooter
name|config
range|:
name|trackingFooters
control|)
block|{
if|if
condition|(
name|footer
operator|.
name|matches
argument_list|(
name|config
operator|.
name|footerKey
argument_list|()
argument_list|)
condition|)
block|{
name|Matcher
name|m
init|=
name|config
operator|.
name|match
argument_list|()
operator|.
name|matcher
argument_list|(
name|footer
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|m
operator|.
name|find
argument_list|()
condition|)
block|{
name|String
name|id
init|=
name|m
operator|.
name|groupCount
argument_list|()
operator|>
literal|0
condition|?
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
else|:
name|m
operator|.
name|group
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|id
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|r
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
return|return
name|r
return|;
block|}
block|}
end_class

end_unit

