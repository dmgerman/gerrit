begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|common
package|;
end_package

begin_class
DECL|class|DiffWebLinkInfo
specifier|public
class|class
name|DiffWebLinkInfo
extends|extends
name|WebLinkInfo
block|{
DECL|field|showOnSideBySideDiffView
specifier|public
name|Boolean
name|showOnSideBySideDiffView
decl_stmt|;
DECL|field|showOnUnifiedDiffView
specifier|public
name|Boolean
name|showOnUnifiedDiffView
decl_stmt|;
DECL|method|forSideBySideDiffView ( String name, String imageUrl, String url, String target)
specifier|public
specifier|static
name|DiffWebLinkInfo
name|forSideBySideDiffView
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|imageUrl
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|target
parameter_list|)
block|{
return|return
operator|new
name|DiffWebLinkInfo
argument_list|(
name|name
argument_list|,
name|imageUrl
argument_list|,
name|url
argument_list|,
name|target
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|forUnifiedDiffView ( String name, String imageUrl, String url, String target)
specifier|public
specifier|static
name|DiffWebLinkInfo
name|forUnifiedDiffView
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|imageUrl
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|target
parameter_list|)
block|{
return|return
operator|new
name|DiffWebLinkInfo
argument_list|(
name|name
argument_list|,
name|imageUrl
argument_list|,
name|url
argument_list|,
name|target
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|forSideBySideAndUnifiedDiffView ( String name, String imageUrl, String url, String target)
specifier|public
specifier|static
name|DiffWebLinkInfo
name|forSideBySideAndUnifiedDiffView
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|imageUrl
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|target
parameter_list|)
block|{
return|return
operator|new
name|DiffWebLinkInfo
argument_list|(
name|name
argument_list|,
name|imageUrl
argument_list|,
name|url
argument_list|,
name|target
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|DiffWebLinkInfo ( String name, String imageUrl, String url, String target, boolean showOnSideBySideDiffView, boolean showOnUnifiedDiffView)
specifier|private
name|DiffWebLinkInfo
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|imageUrl
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|target
parameter_list|,
name|boolean
name|showOnSideBySideDiffView
parameter_list|,
name|boolean
name|showOnUnifiedDiffView
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|imageUrl
argument_list|,
name|url
argument_list|,
name|target
argument_list|)
expr_stmt|;
name|this
operator|.
name|showOnSideBySideDiffView
operator|=
name|showOnSideBySideDiffView
condition|?
literal|true
else|:
literal|null
expr_stmt|;
name|this
operator|.
name|showOnUnifiedDiffView
operator|=
name|showOnUnifiedDiffView
condition|?
literal|true
else|:
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

