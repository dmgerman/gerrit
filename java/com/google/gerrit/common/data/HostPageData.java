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
name|client
operator|.
name|DiffPreferencesInfo
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_comment
comment|/** Data sent as part of the host page, to bootstrap the UI. */
end_comment

begin_class
DECL|class|HostPageData
specifier|public
class|class
name|HostPageData
block|{
comment|/**    * Name of the cookie in which the XSRF token is sent from the server to the client during host    * page bootstrapping.    */
DECL|field|XSRF_COOKIE_NAME
specifier|public
specifier|static
specifier|final
name|String
name|XSRF_COOKIE_NAME
init|=
literal|"XSRF_TOKEN"
decl_stmt|;
comment|/**    * Name of the HTTP header in which the client must send the XSRF token to the server on each    * request.    */
DECL|field|XSRF_HEADER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|XSRF_HEADER_NAME
init|=
literal|"X-Gerrit-Auth"
decl_stmt|;
DECL|field|version
specifier|public
name|String
name|version
decl_stmt|;
DECL|field|accountDiffPref
specifier|public
name|DiffPreferencesInfo
name|accountDiffPref
decl_stmt|;
DECL|field|theme
specifier|public
name|Theme
name|theme
decl_stmt|;
DECL|field|plugins
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|plugins
decl_stmt|;
DECL|field|messages
specifier|public
name|List
argument_list|<
name|Message
argument_list|>
name|messages
decl_stmt|;
DECL|field|pluginsLoadTimeout
specifier|public
name|Integer
name|pluginsLoadTimeout
decl_stmt|;
DECL|field|isNoteDbEnabled
specifier|public
name|boolean
name|isNoteDbEnabled
decl_stmt|;
DECL|field|canLoadInIFrame
specifier|public
name|boolean
name|canLoadInIFrame
decl_stmt|;
DECL|class|Theme
specifier|public
specifier|static
class|class
name|Theme
block|{
DECL|field|backgroundColor
specifier|public
name|String
name|backgroundColor
decl_stmt|;
DECL|field|topMenuColor
specifier|public
name|String
name|topMenuColor
decl_stmt|;
DECL|field|textColor
specifier|public
name|String
name|textColor
decl_stmt|;
DECL|field|trimColor
specifier|public
name|String
name|trimColor
decl_stmt|;
DECL|field|selectionColor
specifier|public
name|String
name|selectionColor
decl_stmt|;
DECL|field|changeTableOutdatedColor
specifier|public
name|String
name|changeTableOutdatedColor
decl_stmt|;
DECL|field|tableOddRowColor
specifier|public
name|String
name|tableOddRowColor
decl_stmt|;
DECL|field|tableEvenRowColor
specifier|public
name|String
name|tableEvenRowColor
decl_stmt|;
block|}
DECL|class|Message
specifier|public
specifier|static
class|class
name|Message
block|{
DECL|field|id
specifier|public
name|String
name|id
decl_stmt|;
DECL|field|redisplay
specifier|public
name|Date
name|redisplay
decl_stmt|;
DECL|field|html
specifier|public
name|String
name|html
decl_stmt|;
block|}
block|}
end_class

end_unit

