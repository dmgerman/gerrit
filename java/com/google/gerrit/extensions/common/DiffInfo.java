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
comment|/* This entity contains information about the diff of a file in a revision. */
end_comment

begin_class
DECL|class|DiffInfo
specifier|public
class|class
name|DiffInfo
block|{
comment|// Meta information about the file on side A
DECL|field|metaA
specifier|public
name|FileMeta
name|metaA
decl_stmt|;
comment|// Meta information about the file on side B
DECL|field|metaB
specifier|public
name|FileMeta
name|metaB
decl_stmt|;
comment|// Intraline status
DECL|field|intralineStatus
specifier|public
name|IntraLineStatus
name|intralineStatus
decl_stmt|;
comment|// The type of change
DECL|field|changeType
specifier|public
name|ChangeType
name|changeType
decl_stmt|;
comment|// A list of strings representing the patch set diff header
DECL|field|diffHeader
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|diffHeader
decl_stmt|;
comment|// The content differences in the file as a list of entities
DECL|field|content
specifier|public
name|List
argument_list|<
name|ContentEntry
argument_list|>
name|content
decl_stmt|;
comment|// Links to the file diff in external sites
DECL|field|webLinks
specifier|public
name|List
argument_list|<
name|DiffWebLinkInfo
argument_list|>
name|webLinks
decl_stmt|;
comment|// Binary file
DECL|field|binary
specifier|public
name|Boolean
name|binary
decl_stmt|;
DECL|enum|IntraLineStatus
specifier|public
enum|enum
name|IntraLineStatus
block|{
DECL|enumConstant|OK
name|OK
block|,
DECL|enumConstant|TIMEOUT
name|TIMEOUT
block|,
DECL|enumConstant|FAILURE
name|FAILURE
block|}
DECL|class|FileMeta
specifier|public
specifier|static
class|class
name|FileMeta
block|{
comment|// The ID of the commit containing the file
DECL|field|commitId
specifier|public
specifier|transient
name|String
name|commitId
decl_stmt|;
comment|// The name of the file
DECL|field|name
specifier|public
name|String
name|name
decl_stmt|;
comment|// The content type of the file
DECL|field|contentType
specifier|public
name|String
name|contentType
decl_stmt|;
comment|// The total number of lines in the file
DECL|field|lines
specifier|public
name|Integer
name|lines
decl_stmt|;
comment|// Links to the file in external sites
DECL|field|webLinks
specifier|public
name|List
argument_list|<
name|WebLinkInfo
argument_list|>
name|webLinks
decl_stmt|;
block|}
DECL|class|ContentEntry
specifier|public
specifier|static
specifier|final
class|class
name|ContentEntry
block|{
comment|// Common lines to both sides.
DECL|field|ab
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|ab
decl_stmt|;
comment|// Lines of a.
DECL|field|a
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|a
decl_stmt|;
comment|// Lines of b.
DECL|field|b
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|b
decl_stmt|;
comment|// A list of changed sections of the corresponding line list.
comment|// Each entry is a character<offset, length> pair. The offset is from the
comment|// beginning of the first line in the list. Also, the offset includes an
comment|// implied trailing newline character for each line.
DECL|field|editA
specifier|public
name|List
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|editA
decl_stmt|;
DECL|field|editB
specifier|public
name|List
argument_list|<
name|List
argument_list|<
name|Integer
argument_list|>
argument_list|>
name|editB
decl_stmt|;
comment|// Indicates that this entry only exists because of a rebase (and not because of a real change
comment|// between 'a' and 'b').
DECL|field|dueToRebase
specifier|public
name|Boolean
name|dueToRebase
decl_stmt|;
comment|// a and b are actually common with this whitespace ignore setting.
DECL|field|common
specifier|public
name|Boolean
name|common
decl_stmt|;
comment|// Number of lines to skip on both sides.
DECL|field|skip
specifier|public
name|Integer
name|skip
decl_stmt|;
block|}
block|}
end_class

end_unit

