begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.common.data
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|data
package|;
end_package

begin_class
DECL|class|GerritConfig
specifier|public
class|class
name|GerritConfig
implements|implements
name|Cloneable
block|{
DECL|field|reportBugUrl
specifier|protected
name|String
name|reportBugUrl
decl_stmt|;
DECL|field|reportBugText
specifier|protected
name|String
name|reportBugText
decl_stmt|;
DECL|field|gitweb
specifier|protected
name|GitwebConfig
name|gitweb
decl_stmt|;
DECL|field|gitDaemonUrl
specifier|protected
name|String
name|gitDaemonUrl
decl_stmt|;
DECL|field|sshdAddress
specifier|protected
name|String
name|sshdAddress
decl_stmt|;
DECL|field|documentationAvailable
specifier|protected
name|boolean
name|documentationAvailable
decl_stmt|;
DECL|field|anonymousCowardName
specifier|protected
name|String
name|anonymousCowardName
decl_stmt|;
DECL|field|suggestFrom
specifier|protected
name|int
name|suggestFrom
decl_stmt|;
DECL|field|changeUpdateDelay
specifier|protected
name|int
name|changeUpdateDelay
decl_stmt|;
DECL|field|largeChangeSize
specifier|protected
name|int
name|largeChangeSize
decl_stmt|;
DECL|field|replyLabel
specifier|protected
name|String
name|replyLabel
decl_stmt|;
DECL|field|replyTitle
specifier|protected
name|String
name|replyTitle
decl_stmt|;
DECL|field|allowDraftChanges
specifier|protected
name|boolean
name|allowDraftChanges
decl_stmt|;
DECL|method|getReportBugUrl ()
specifier|public
name|String
name|getReportBugUrl
parameter_list|()
block|{
return|return
name|reportBugUrl
return|;
block|}
DECL|method|setReportBugUrl (String u)
specifier|public
name|void
name|setReportBugUrl
parameter_list|(
name|String
name|u
parameter_list|)
block|{
name|reportBugUrl
operator|=
name|u
expr_stmt|;
block|}
DECL|method|getReportBugText ()
specifier|public
name|String
name|getReportBugText
parameter_list|()
block|{
return|return
name|reportBugText
return|;
block|}
DECL|method|setReportBugText (String t)
specifier|public
name|void
name|setReportBugText
parameter_list|(
name|String
name|t
parameter_list|)
block|{
name|reportBugText
operator|=
name|t
expr_stmt|;
block|}
DECL|method|getGitwebLink ()
specifier|public
name|GitwebConfig
name|getGitwebLink
parameter_list|()
block|{
return|return
name|gitweb
return|;
block|}
DECL|method|setGitwebLink (final GitwebConfig w)
specifier|public
name|void
name|setGitwebLink
parameter_list|(
specifier|final
name|GitwebConfig
name|w
parameter_list|)
block|{
name|gitweb
operator|=
name|w
expr_stmt|;
block|}
DECL|method|getGitDaemonUrl ()
specifier|public
name|String
name|getGitDaemonUrl
parameter_list|()
block|{
return|return
name|gitDaemonUrl
return|;
block|}
DECL|method|setGitDaemonUrl (String url)
specifier|public
name|void
name|setGitDaemonUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
if|if
condition|(
name|url
operator|!=
literal|null
operator|&&
operator|!
name|url
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"/"
expr_stmt|;
block|}
name|gitDaemonUrl
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getSshdAddress ()
specifier|public
name|String
name|getSshdAddress
parameter_list|()
block|{
return|return
name|sshdAddress
return|;
block|}
DECL|method|setSshdAddress (final String addr)
specifier|public
name|void
name|setSshdAddress
parameter_list|(
specifier|final
name|String
name|addr
parameter_list|)
block|{
name|sshdAddress
operator|=
name|addr
expr_stmt|;
block|}
DECL|method|isDocumentationAvailable ()
specifier|public
name|boolean
name|isDocumentationAvailable
parameter_list|()
block|{
return|return
name|documentationAvailable
return|;
block|}
DECL|method|setDocumentationAvailable (final boolean available)
specifier|public
name|void
name|setDocumentationAvailable
parameter_list|(
specifier|final
name|boolean
name|available
parameter_list|)
block|{
name|documentationAvailable
operator|=
name|available
expr_stmt|;
block|}
DECL|method|getAnonymousCowardName ()
specifier|public
name|String
name|getAnonymousCowardName
parameter_list|()
block|{
return|return
name|anonymousCowardName
return|;
block|}
DECL|method|setAnonymousCowardName (final String anonymousCowardName)
specifier|public
name|void
name|setAnonymousCowardName
parameter_list|(
specifier|final
name|String
name|anonymousCowardName
parameter_list|)
block|{
name|this
operator|.
name|anonymousCowardName
operator|=
name|anonymousCowardName
expr_stmt|;
block|}
DECL|method|getSuggestFrom ()
specifier|public
name|int
name|getSuggestFrom
parameter_list|()
block|{
return|return
name|suggestFrom
return|;
block|}
DECL|method|setSuggestFrom (final int suggestFrom)
specifier|public
name|void
name|setSuggestFrom
parameter_list|(
specifier|final
name|int
name|suggestFrom
parameter_list|)
block|{
name|this
operator|.
name|suggestFrom
operator|=
name|suggestFrom
expr_stmt|;
block|}
DECL|method|getChangeUpdateDelay ()
specifier|public
name|int
name|getChangeUpdateDelay
parameter_list|()
block|{
return|return
name|changeUpdateDelay
return|;
block|}
DECL|method|setChangeUpdateDelay (int seconds)
specifier|public
name|void
name|setChangeUpdateDelay
parameter_list|(
name|int
name|seconds
parameter_list|)
block|{
name|changeUpdateDelay
operator|=
name|seconds
expr_stmt|;
block|}
DECL|method|getLargeChangeSize ()
specifier|public
name|int
name|getLargeChangeSize
parameter_list|()
block|{
return|return
name|largeChangeSize
return|;
block|}
DECL|method|setLargeChangeSize (int largeChangeSize)
specifier|public
name|void
name|setLargeChangeSize
parameter_list|(
name|int
name|largeChangeSize
parameter_list|)
block|{
name|this
operator|.
name|largeChangeSize
operator|=
name|largeChangeSize
expr_stmt|;
block|}
DECL|method|getReplyTitle ()
specifier|public
name|String
name|getReplyTitle
parameter_list|()
block|{
return|return
name|replyTitle
return|;
block|}
DECL|method|setReplyTitle (String r)
specifier|public
name|void
name|setReplyTitle
parameter_list|(
name|String
name|r
parameter_list|)
block|{
name|replyTitle
operator|=
name|r
expr_stmt|;
block|}
DECL|method|getReplyLabel ()
specifier|public
name|String
name|getReplyLabel
parameter_list|()
block|{
return|return
name|replyLabel
return|;
block|}
DECL|method|setReplyLabel (String r)
specifier|public
name|void
name|setReplyLabel
parameter_list|(
name|String
name|r
parameter_list|)
block|{
name|replyLabel
operator|=
name|r
expr_stmt|;
block|}
DECL|method|isAllowDraftChanges ()
specifier|public
name|boolean
name|isAllowDraftChanges
parameter_list|()
block|{
return|return
name|allowDraftChanges
return|;
block|}
DECL|method|setAllowDraftChanges (boolean b)
specifier|public
name|void
name|setAllowDraftChanges
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|allowDraftChanges
operator|=
name|b
expr_stmt|;
block|}
block|}
end_class

end_unit

