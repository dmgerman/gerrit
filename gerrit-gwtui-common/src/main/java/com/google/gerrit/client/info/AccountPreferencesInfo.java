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
DECL|package|com.google.gerrit.client.info
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|client
operator|.
name|info
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
operator|.
name|DateFormat
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
operator|.
name|DiffView
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
operator|.
name|DownloadCommand
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
operator|.
name|DownloadScheme
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
operator|.
name|ReviewCategoryStrategy
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
name|reviewdb
operator|.
name|client
operator|.
name|AccountGeneralPreferences
operator|.
name|TimeFormat
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JavaScriptObject
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|client
operator|.
name|JsArray
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

begin_class
DECL|class|AccountPreferencesInfo
specifier|public
class|class
name|AccountPreferencesInfo
extends|extends
name|JavaScriptObject
block|{
DECL|method|create ()
specifier|public
specifier|static
name|AccountPreferencesInfo
name|create
parameter_list|()
block|{
return|return
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
return|;
block|}
DECL|method|createDefault ()
specifier|public
specifier|static
name|AccountPreferencesInfo
name|createDefault
parameter_list|()
block|{
name|AccountGeneralPreferences
name|defaultPrefs
init|=
name|AccountGeneralPreferences
operator|.
name|createDefault
argument_list|()
decl_stmt|;
name|AccountPreferencesInfo
name|p
init|=
name|createObject
argument_list|()
operator|.
name|cast
argument_list|()
decl_stmt|;
name|p
operator|.
name|changesPerPage
argument_list|(
name|defaultPrefs
operator|.
name|getMaximumPageSize
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|showSiteHeader
argument_list|(
name|defaultPrefs
operator|.
name|isShowSiteHeader
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|useFlashClipboard
argument_list|(
name|defaultPrefs
operator|.
name|isUseFlashClipboard
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|downloadScheme
argument_list|(
name|defaultPrefs
operator|.
name|getDownloadUrl
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|downloadCommand
argument_list|(
name|defaultPrefs
operator|.
name|getDownloadCommand
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|copySelfOnEmail
argument_list|(
name|defaultPrefs
operator|.
name|isCopySelfOnEmails
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|dateFormat
argument_list|(
name|defaultPrefs
operator|.
name|getDateFormat
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|timeFormat
argument_list|(
name|defaultPrefs
operator|.
name|getTimeFormat
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|relativeDateInChangeTable
argument_list|(
name|defaultPrefs
operator|.
name|isRelativeDateInChangeTable
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|sizeBarInChangeTable
argument_list|(
name|defaultPrefs
operator|.
name|isSizeBarInChangeTable
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|legacycidInChangeTable
argument_list|(
name|defaultPrefs
operator|.
name|isLegacycidInChangeTable
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|muteCommonPathPrefixes
argument_list|(
name|defaultPrefs
operator|.
name|isMuteCommonPathPrefixes
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|reviewCategoryStrategy
argument_list|(
name|defaultPrefs
operator|.
name|getReviewCategoryStrategy
argument_list|()
argument_list|)
expr_stmt|;
name|p
operator|.
name|diffView
argument_list|(
name|defaultPrefs
operator|.
name|getDiffView
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|p
return|;
block|}
DECL|method|changesPerPage ()
specifier|public
specifier|final
name|short
name|changesPerPage
parameter_list|()
block|{
name|short
name|changesPerPage
init|=
name|get
argument_list|(
literal|"changes_per_page"
argument_list|,
name|AccountGeneralPreferences
operator|.
name|DEFAULT_PAGESIZE
argument_list|)
decl_stmt|;
return|return
literal|0
operator|<
name|changesPerPage
condition|?
name|changesPerPage
else|:
name|AccountGeneralPreferences
operator|.
name|DEFAULT_PAGESIZE
return|;
block|}
DECL|method|get (String n, int d)
specifier|private
specifier|final
specifier|native
name|short
name|get
parameter_list|(
name|String
name|n
parameter_list|,
name|int
name|d
parameter_list|)
comment|/*-{ return this.hasOwnProperty(n) ? this[n] : d }-*/
function_decl|;
DECL|method|showSiteHeader ()
specifier|public
specifier|final
specifier|native
name|boolean
name|showSiteHeader
parameter_list|()
comment|/*-{ return this.show_site_header || false }-*/
function_decl|;
DECL|method|useFlashClipboard ()
specifier|public
specifier|final
specifier|native
name|boolean
name|useFlashClipboard
parameter_list|()
comment|/*-{ return this.use_flash_clipboard || false }-*/
function_decl|;
DECL|method|downloadScheme ()
specifier|public
specifier|final
name|DownloadScheme
name|downloadScheme
parameter_list|()
block|{
name|String
name|s
init|=
name|downloadSchemeRaw
argument_list|()
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|DownloadScheme
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|downloadSchemeRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|downloadSchemeRaw
parameter_list|()
comment|/*-{ return this.download_scheme }-*/
function_decl|;
DECL|method|downloadCommand ()
specifier|public
specifier|final
name|DownloadCommand
name|downloadCommand
parameter_list|()
block|{
name|String
name|s
init|=
name|downloadCommandRaw
argument_list|()
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|DownloadCommand
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|downloadCommandRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|downloadCommandRaw
parameter_list|()
comment|/*-{ return this.download_command }-*/
function_decl|;
DECL|method|copySelfOnEmail ()
specifier|public
specifier|final
specifier|native
name|boolean
name|copySelfOnEmail
parameter_list|()
comment|/*-{ return this.copy_self_on_email || false }-*/
function_decl|;
DECL|method|dateFormat ()
specifier|public
specifier|final
name|DateFormat
name|dateFormat
parameter_list|()
block|{
name|String
name|s
init|=
name|dateFormatRaw
argument_list|()
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|DateFormat
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|dateFormatRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|dateFormatRaw
parameter_list|()
comment|/*-{ return this.date_format }-*/
function_decl|;
DECL|method|timeFormat ()
specifier|public
specifier|final
name|TimeFormat
name|timeFormat
parameter_list|()
block|{
name|String
name|s
init|=
name|timeFormatRaw
argument_list|()
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|TimeFormat
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|timeFormatRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|timeFormatRaw
parameter_list|()
comment|/*-{ return this.time_format }-*/
function_decl|;
DECL|method|relativeDateInChangeTable ()
specifier|public
specifier|final
specifier|native
name|boolean
name|relativeDateInChangeTable
parameter_list|()
comment|/*-{ return this.relative_date_in_change_table || false }-*/
function_decl|;
DECL|method|sizeBarInChangeTable ()
specifier|public
specifier|final
specifier|native
name|boolean
name|sizeBarInChangeTable
parameter_list|()
comment|/*-{ return this.size_bar_in_change_table || false }-*/
function_decl|;
DECL|method|legacycidInChangeTable ()
specifier|public
specifier|final
specifier|native
name|boolean
name|legacycidInChangeTable
parameter_list|()
comment|/*-{ return this.legacycid_in_change_table || false }-*/
function_decl|;
DECL|method|muteCommonPathPrefixes ()
specifier|public
specifier|final
specifier|native
name|boolean
name|muteCommonPathPrefixes
parameter_list|()
comment|/*-{ return this.mute_common_path_prefixes || false }-*/
function_decl|;
DECL|method|reviewCategoryStrategy ()
specifier|public
specifier|final
name|ReviewCategoryStrategy
name|reviewCategoryStrategy
parameter_list|()
block|{
name|String
name|s
init|=
name|reviewCategeoryStrategyRaw
argument_list|()
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|ReviewCategoryStrategy
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
else|:
name|ReviewCategoryStrategy
operator|.
name|NONE
return|;
block|}
DECL|method|reviewCategeoryStrategyRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|reviewCategeoryStrategyRaw
parameter_list|()
comment|/*-{ return this.review_category_strategy }-*/
function_decl|;
DECL|method|diffView ()
specifier|public
specifier|final
name|DiffView
name|diffView
parameter_list|()
block|{
name|String
name|s
init|=
name|diffViewRaw
argument_list|()
decl_stmt|;
return|return
name|s
operator|!=
literal|null
condition|?
name|DiffView
operator|.
name|valueOf
argument_list|(
name|s
argument_list|)
else|:
literal|null
return|;
block|}
DECL|method|diffViewRaw ()
specifier|private
specifier|final
specifier|native
name|String
name|diffViewRaw
parameter_list|()
comment|/*-{ return this.diff_view }-*/
function_decl|;
DECL|method|my ()
specifier|public
specifier|final
specifier|native
name|JsArray
argument_list|<
name|TopMenuItem
argument_list|>
name|my
parameter_list|()
comment|/*-{ return this.my; }-*/
function_decl|;
DECL|method|changesPerPage (short n)
specifier|public
specifier|final
specifier|native
name|void
name|changesPerPage
parameter_list|(
name|short
name|n
parameter_list|)
comment|/*-{ this.changes_per_page = n }-*/
function_decl|;
DECL|method|showSiteHeader (boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|showSiteHeader
parameter_list|(
name|boolean
name|s
parameter_list|)
comment|/*-{ this.show_site_header = s }-*/
function_decl|;
DECL|method|useFlashClipboard (boolean u)
specifier|public
specifier|final
specifier|native
name|void
name|useFlashClipboard
parameter_list|(
name|boolean
name|u
parameter_list|)
comment|/*-{ this.use_flash_clipboard = u }-*/
function_decl|;
DECL|method|downloadScheme (DownloadScheme d)
specifier|public
specifier|final
name|void
name|downloadScheme
parameter_list|(
name|DownloadScheme
name|d
parameter_list|)
block|{
name|downloadSchemeRaw
argument_list|(
name|d
operator|!=
literal|null
condition|?
name|d
operator|.
name|toString
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|downloadSchemeRaw (String d)
specifier|private
specifier|final
specifier|native
name|void
name|downloadSchemeRaw
parameter_list|(
name|String
name|d
parameter_list|)
comment|/*-{ this.download_scheme = d }-*/
function_decl|;
DECL|method|downloadCommand (DownloadCommand d)
specifier|public
specifier|final
name|void
name|downloadCommand
parameter_list|(
name|DownloadCommand
name|d
parameter_list|)
block|{
name|downloadCommandRaw
argument_list|(
name|d
operator|!=
literal|null
condition|?
name|d
operator|.
name|toString
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|downloadCommandRaw (String d)
specifier|public
specifier|final
specifier|native
name|void
name|downloadCommandRaw
parameter_list|(
name|String
name|d
parameter_list|)
comment|/*-{ this.download_command = d }-*/
function_decl|;
DECL|method|copySelfOnEmail (boolean c)
specifier|public
specifier|final
specifier|native
name|void
name|copySelfOnEmail
parameter_list|(
name|boolean
name|c
parameter_list|)
comment|/*-{ this.copy_self_on_email = c }-*/
function_decl|;
DECL|method|dateFormat (DateFormat f)
specifier|public
specifier|final
name|void
name|dateFormat
parameter_list|(
name|DateFormat
name|f
parameter_list|)
block|{
name|dateFormatRaw
argument_list|(
name|f
operator|!=
literal|null
condition|?
name|f
operator|.
name|toString
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|dateFormatRaw (String f)
specifier|private
specifier|final
specifier|native
name|void
name|dateFormatRaw
parameter_list|(
name|String
name|f
parameter_list|)
comment|/*-{ this.date_format = f }-*/
function_decl|;
DECL|method|timeFormat (TimeFormat f)
specifier|public
specifier|final
name|void
name|timeFormat
parameter_list|(
name|TimeFormat
name|f
parameter_list|)
block|{
name|timeFormatRaw
argument_list|(
name|f
operator|!=
literal|null
condition|?
name|f
operator|.
name|toString
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|timeFormatRaw (String f)
specifier|private
specifier|final
specifier|native
name|void
name|timeFormatRaw
parameter_list|(
name|String
name|f
parameter_list|)
comment|/*-{ this.time_format = f }-*/
function_decl|;
DECL|method|relativeDateInChangeTable (boolean d)
specifier|public
specifier|final
specifier|native
name|void
name|relativeDateInChangeTable
parameter_list|(
name|boolean
name|d
parameter_list|)
comment|/*-{ this.relative_date_in_change_table = d }-*/
function_decl|;
DECL|method|sizeBarInChangeTable (boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|sizeBarInChangeTable
parameter_list|(
name|boolean
name|s
parameter_list|)
comment|/*-{ this.size_bar_in_change_table = s }-*/
function_decl|;
DECL|method|legacycidInChangeTable (boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|legacycidInChangeTable
parameter_list|(
name|boolean
name|s
parameter_list|)
comment|/*-{ this.legacycid_in_change_table = s }-*/
function_decl|;
DECL|method|muteCommonPathPrefixes (boolean s)
specifier|public
specifier|final
specifier|native
name|void
name|muteCommonPathPrefixes
parameter_list|(
name|boolean
name|s
parameter_list|)
comment|/*-{ this.mute_common_path_prefixes = s }-*/
function_decl|;
DECL|method|reviewCategoryStrategy (ReviewCategoryStrategy s)
specifier|public
specifier|final
name|void
name|reviewCategoryStrategy
parameter_list|(
name|ReviewCategoryStrategy
name|s
parameter_list|)
block|{
name|reviewCategoryStrategyRaw
argument_list|(
name|s
operator|!=
literal|null
condition|?
name|s
operator|.
name|toString
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|reviewCategoryStrategyRaw (String s)
specifier|private
specifier|final
specifier|native
name|void
name|reviewCategoryStrategyRaw
parameter_list|(
name|String
name|s
parameter_list|)
comment|/*-{ this.review_category_strategy = s }-*/
function_decl|;
DECL|method|diffView (DiffView d)
specifier|public
specifier|final
name|void
name|diffView
parameter_list|(
name|DiffView
name|d
parameter_list|)
block|{
name|diffViewRaw
argument_list|(
name|d
operator|!=
literal|null
condition|?
name|d
operator|.
name|toString
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|diffViewRaw (String d)
specifier|private
specifier|final
specifier|native
name|void
name|diffViewRaw
parameter_list|(
name|String
name|d
parameter_list|)
comment|/*-{ this.diff_view = d }-*/
function_decl|;
DECL|method|setMyMenus (List<TopMenuItem> myMenus)
specifier|public
specifier|final
name|void
name|setMyMenus
parameter_list|(
name|List
argument_list|<
name|TopMenuItem
argument_list|>
name|myMenus
parameter_list|)
block|{
name|initMy
argument_list|()
expr_stmt|;
for|for
control|(
name|TopMenuItem
name|n
range|:
name|myMenus
control|)
block|{
name|addMy
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|initMy ()
specifier|final
specifier|native
name|void
name|initMy
parameter_list|()
comment|/*-{ this.my = []; }-*/
function_decl|;
DECL|method|addMy (TopMenuItem m)
specifier|final
specifier|native
name|void
name|addMy
parameter_list|(
name|TopMenuItem
name|m
parameter_list|)
comment|/*-{ this.my.push(m); }-*/
function_decl|;
DECL|method|AccountPreferencesInfo ()
specifier|protected
name|AccountPreferencesInfo
parameter_list|()
block|{   }
block|}
end_class

end_unit

