begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.patch
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|patch
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableList
operator|.
name|toImmutableList
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|Multimaps
operator|.
name|toMultimap
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Comparator
operator|.
name|comparing
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|groupingBy
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
operator|.
name|toList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
import|;
end_import

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
name|ArrayListMultimap
import|;
end_import

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
name|ImmutableList
import|;
end_import

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
name|Multimap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|diff
operator|.
name|Edit
import|;
end_import

begin_comment
comment|/**  * Transformer of edits regarding their base trees. An edit describes a difference between {@code  * treeA} and {@code treeB}. This class allows to describe the edit as a difference between {@code  * treeA'} and {@code treeB'} given the transformation of {@code treeA} to {@code treeA'} and {@code  * treeB} to {@code treeB'}. Edits which can't be transformed due to conflicts with the  * transformation are omitted.  */
end_comment

begin_class
DECL|class|EditTransformer
class|class
name|EditTransformer
block|{
DECL|field|edits
specifier|private
name|List
argument_list|<
name|ContextAwareEdit
argument_list|>
name|edits
decl_stmt|;
comment|/**    * Creates a new {@code EditTransformer} for the edits contained in the specified {@code    * PatchListEntry}s.    *    * @param patchListEntries a list of {@code PatchListEntry}s containing the edits    */
DECL|method|EditTransformer (List<PatchListEntry> patchListEntries)
specifier|public
name|EditTransformer
parameter_list|(
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|patchListEntries
parameter_list|)
block|{
name|edits
operator|=
name|patchListEntries
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|EditTransformer
operator|::
name|toEdits
argument_list|)
operator|.
name|collect
argument_list|(
name|toImmutableList
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Transforms the references of side A of the edits. If the edits describe differences between    * {@code treeA} and {@code treeB} and the specified {@code PatchListEntry}s define a    * transformation from {@code treeA} to {@code treeA'}, the resulting edits will be defined as    * differences between {@code treeA'} and {@code treeB}. Edits which can't be transformed due to    * conflicts with the transformation are omitted.    *    * @param transformationEntries a list of {@code PatchListEntry}s defining the transformation of    *     {@code treeA} to {@code treeA'}    */
DECL|method|transformReferencesOfSideA (List<PatchListEntry> transformationEntries)
specifier|public
name|void
name|transformReferencesOfSideA
parameter_list|(
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|transformationEntries
parameter_list|)
block|{
name|transformEdits
argument_list|(
name|transformationEntries
argument_list|,
name|SideAStrategy
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
comment|/**    * Transforms the references of side B of the edits. If the edits describe differences between    * {@code treeA} and {@code treeB} and the specified {@code PatchListEntry}s define a    * transformation from {@code treeB} to {@code treeB'}, the resulting edits will be defined as    * differences between {@code treeA} and {@code treeB'}. Edits which can't be transformed due to    * conflicts with the transformation are omitted.    *    * @param transformationEntries a list of {@code PatchListEntry}s defining the transformation of    *     {@code treeB} to {@code treeB'}    */
DECL|method|transformReferencesOfSideB (List<PatchListEntry> transformationEntries)
specifier|public
name|void
name|transformReferencesOfSideB
parameter_list|(
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|transformationEntries
parameter_list|)
block|{
name|transformEdits
argument_list|(
name|transformationEntries
argument_list|,
name|SideBStrategy
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
comment|/**    * Returns the transformed edits per file path they modify in {@code treeB'}.    *    * @return the transformed edits per file path    */
DECL|method|getEditsPerFilePath ()
specifier|public
name|Multimap
argument_list|<
name|String
argument_list|,
name|ContextAwareEdit
argument_list|>
name|getEditsPerFilePath
parameter_list|()
block|{
return|return
name|edits
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|toMultimap
argument_list|(
name|ContextAwareEdit
operator|::
name|getNewFilePath
argument_list|,
name|Function
operator|.
name|identity
argument_list|()
argument_list|,
name|ArrayListMultimap
operator|::
name|create
argument_list|)
argument_list|)
return|;
block|}
DECL|method|toEdits (PatchListEntry patchListEntry)
specifier|public
specifier|static
name|Stream
argument_list|<
name|ContextAwareEdit
argument_list|>
name|toEdits
parameter_list|(
name|PatchListEntry
name|patchListEntry
parameter_list|)
block|{
name|ImmutableList
argument_list|<
name|Edit
argument_list|>
name|edits
init|=
name|patchListEntry
operator|.
name|getEdits
argument_list|()
decl_stmt|;
if|if
condition|(
name|edits
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|Stream
operator|.
name|of
argument_list|(
name|ContextAwareEdit
operator|.
name|createForNoContentEdit
argument_list|(
name|patchListEntry
argument_list|)
argument_list|)
return|;
block|}
return|return
name|edits
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|edit
lambda|->
name|ContextAwareEdit
operator|.
name|create
argument_list|(
name|patchListEntry
argument_list|,
name|edit
argument_list|)
argument_list|)
return|;
block|}
DECL|method|transformEdits (List<PatchListEntry> transformingEntries, SideStrategy sideStrategy)
specifier|private
name|void
name|transformEdits
parameter_list|(
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|transformingEntries
parameter_list|,
name|SideStrategy
name|sideStrategy
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ContextAwareEdit
argument_list|>
argument_list|>
name|editsPerFilePath
init|=
name|edits
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|groupingBy
argument_list|(
name|sideStrategy
operator|::
name|getFilePath
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|PatchListEntry
argument_list|>
argument_list|>
name|transEntriesPerPath
init|=
name|transformingEntries
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|groupingBy
argument_list|(
name|EditTransformer
operator|::
name|getOldFilePath
argument_list|)
argument_list|)
decl_stmt|;
name|edits
operator|=
name|editsPerFilePath
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|pathAndEdits
lambda|->
block|{
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|transEntries
init|=
name|transEntriesPerPath
operator|.
name|getOrDefault
argument_list|(
name|pathAndEdits
operator|.
name|getKey
argument_list|()
argument_list|,
name|ImmutableList
operator|.
name|of
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|transformEdits
argument_list|(
name|sideStrategy
argument_list|,
name|pathAndEdits
operator|.
name|getValue
argument_list|()
argument_list|,
name|transEntries
argument_list|)
return|;
block|}
argument_list|)
operator|.
name|collect
argument_list|(
name|toList
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getOldFilePath (PatchListEntry patchListEntry)
specifier|private
specifier|static
name|String
name|getOldFilePath
parameter_list|(
name|PatchListEntry
name|patchListEntry
parameter_list|)
block|{
return|return
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|patchListEntry
operator|.
name|getOldName
argument_list|()
argument_list|,
name|patchListEntry
operator|.
name|getNewName
argument_list|()
argument_list|)
return|;
block|}
DECL|method|transformEdits ( SideStrategy sideStrategy, List<ContextAwareEdit> originalEdits, List<PatchListEntry> transformingEntries)
specifier|private
specifier|static
name|Stream
argument_list|<
name|ContextAwareEdit
argument_list|>
name|transformEdits
parameter_list|(
name|SideStrategy
name|sideStrategy
parameter_list|,
name|List
argument_list|<
name|ContextAwareEdit
argument_list|>
name|originalEdits
parameter_list|,
name|List
argument_list|<
name|PatchListEntry
argument_list|>
name|transformingEntries
parameter_list|)
block|{
if|if
condition|(
name|transformingEntries
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|originalEdits
operator|.
name|stream
argument_list|()
return|;
block|}
comment|// TODO(aliceks): Find a way to prevent an explosion of the number of entries.
return|return
name|transformingEntries
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|transEntry
lambda|->
name|transformEdits
argument_list|(
name|sideStrategy
argument_list|,
name|originalEdits
argument_list|,
name|transEntry
operator|.
name|getEdits
argument_list|()
argument_list|,
name|transEntry
operator|.
name|getNewName
argument_list|()
argument_list|)
operator|.
name|stream
argument_list|()
argument_list|)
return|;
block|}
DECL|method|transformEdits ( SideStrategy sideStrategy, List<ContextAwareEdit> unorderedOriginalEdits, List<Edit> unorderedTransformingEdits, String adjustedFilePath)
specifier|private
specifier|static
name|List
argument_list|<
name|ContextAwareEdit
argument_list|>
name|transformEdits
parameter_list|(
name|SideStrategy
name|sideStrategy
parameter_list|,
name|List
argument_list|<
name|ContextAwareEdit
argument_list|>
name|unorderedOriginalEdits
parameter_list|,
name|List
argument_list|<
name|Edit
argument_list|>
name|unorderedTransformingEdits
parameter_list|,
name|String
name|adjustedFilePath
parameter_list|)
block|{
name|List
argument_list|<
name|ContextAwareEdit
argument_list|>
name|originalEdits
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|unorderedOriginalEdits
argument_list|)
decl_stmt|;
name|originalEdits
operator|.
name|sort
argument_list|(
name|comparing
argument_list|(
name|sideStrategy
operator|::
name|getBegin
argument_list|)
operator|.
name|thenComparing
argument_list|(
name|sideStrategy
operator|::
name|getEnd
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Edit
argument_list|>
name|transformingEdits
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|unorderedTransformingEdits
argument_list|)
decl_stmt|;
name|transformingEdits
operator|.
name|sort
argument_list|(
name|comparing
argument_list|(
name|Edit
operator|::
name|getBeginA
argument_list|)
operator|.
name|thenComparing
argument_list|(
name|Edit
operator|::
name|getEndA
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|shiftedAmount
init|=
literal|0
decl_stmt|;
name|int
name|transIndex
init|=
literal|0
decl_stmt|;
name|int
name|origIndex
init|=
literal|0
decl_stmt|;
name|List
argument_list|<
name|ContextAwareEdit
argument_list|>
name|resultingEdits
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|originalEdits
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|origIndex
operator|<
name|originalEdits
operator|.
name|size
argument_list|()
operator|&&
name|transIndex
operator|<
name|transformingEdits
operator|.
name|size
argument_list|()
condition|)
block|{
name|ContextAwareEdit
name|originalEdit
init|=
name|originalEdits
operator|.
name|get
argument_list|(
name|origIndex
argument_list|)
decl_stmt|;
name|Edit
name|transformingEdit
init|=
name|transformingEdits
operator|.
name|get
argument_list|(
name|transIndex
argument_list|)
decl_stmt|;
if|if
condition|(
name|transformingEdit
operator|.
name|getEndA
argument_list|()
operator|<
name|sideStrategy
operator|.
name|getBegin
argument_list|(
name|originalEdit
argument_list|)
condition|)
block|{
name|shiftedAmount
operator|=
name|transformingEdit
operator|.
name|getEndB
argument_list|()
operator|-
name|transformingEdit
operator|.
name|getEndA
argument_list|()
expr_stmt|;
name|transIndex
operator|++
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|sideStrategy
operator|.
name|getEnd
argument_list|(
name|originalEdit
argument_list|)
operator|<
name|transformingEdit
operator|.
name|getBeginA
argument_list|()
condition|)
block|{
name|resultingEdits
operator|.
name|add
argument_list|(
name|sideStrategy
operator|.
name|create
argument_list|(
name|originalEdit
argument_list|,
name|shiftedAmount
argument_list|,
name|adjustedFilePath
argument_list|)
argument_list|)
expr_stmt|;
name|origIndex
operator|++
expr_stmt|;
block|}
else|else
block|{
comment|// Overlapping -> ignore.
name|origIndex
operator|++
expr_stmt|;
block|}
block|}
for|for
control|(
name|int
name|i
init|=
name|origIndex
init|;
name|i
operator|<
name|originalEdits
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|resultingEdits
operator|.
name|add
argument_list|(
name|sideStrategy
operator|.
name|create
argument_list|(
name|originalEdits
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|,
name|shiftedAmount
argument_list|,
name|adjustedFilePath
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|resultingEdits
return|;
block|}
annotation|@
name|AutoValue
DECL|class|ContextAwareEdit
specifier|abstract
specifier|static
class|class
name|ContextAwareEdit
block|{
DECL|method|create (PatchListEntry patchListEntry, Edit edit)
specifier|static
name|ContextAwareEdit
name|create
parameter_list|(
name|PatchListEntry
name|patchListEntry
parameter_list|,
name|Edit
name|edit
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|patchListEntry
operator|.
name|getOldName
argument_list|()
argument_list|,
name|patchListEntry
operator|.
name|getNewName
argument_list|()
argument_list|,
name|edit
operator|.
name|getBeginA
argument_list|()
argument_list|,
name|edit
operator|.
name|getEndA
argument_list|()
argument_list|,
name|edit
operator|.
name|getBeginB
argument_list|()
argument_list|,
name|edit
operator|.
name|getEndB
argument_list|()
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|createForNoContentEdit (PatchListEntry patchListEntry)
specifier|static
name|ContextAwareEdit
name|createForNoContentEdit
parameter_list|(
name|PatchListEntry
name|patchListEntry
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|patchListEntry
operator|.
name|getOldName
argument_list|()
argument_list|,
name|patchListEntry
operator|.
name|getNewName
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|,
operator|-
literal|1
argument_list|,
literal|false
argument_list|)
return|;
block|}
DECL|method|create ( String oldFilePath, String newFilePath, int beginA, int endA, int beginB, int endB, boolean filePathAdjusted)
specifier|static
name|ContextAwareEdit
name|create
parameter_list|(
name|String
name|oldFilePath
parameter_list|,
name|String
name|newFilePath
parameter_list|,
name|int
name|beginA
parameter_list|,
name|int
name|endA
parameter_list|,
name|int
name|beginB
parameter_list|,
name|int
name|endB
parameter_list|,
name|boolean
name|filePathAdjusted
parameter_list|)
block|{
name|String
name|adjustedOldFilePath
init|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|oldFilePath
argument_list|,
name|newFilePath
argument_list|)
decl_stmt|;
name|boolean
name|implicitRename
init|=
operator|!
name|Objects
operator|.
name|equals
argument_list|(
name|oldFilePath
argument_list|,
name|newFilePath
argument_list|)
operator|&&
name|filePathAdjusted
decl_stmt|;
return|return
operator|new
name|AutoValue_EditTransformer_ContextAwareEdit
argument_list|(
name|adjustedOldFilePath
argument_list|,
name|newFilePath
argument_list|,
name|beginA
argument_list|,
name|endA
argument_list|,
name|beginB
argument_list|,
name|endB
argument_list|,
name|implicitRename
argument_list|)
return|;
block|}
DECL|method|getOldFilePath ()
specifier|public
specifier|abstract
name|String
name|getOldFilePath
parameter_list|()
function_decl|;
DECL|method|getNewFilePath ()
specifier|public
specifier|abstract
name|String
name|getNewFilePath
parameter_list|()
function_decl|;
DECL|method|getBeginA ()
specifier|public
specifier|abstract
name|int
name|getBeginA
parameter_list|()
function_decl|;
DECL|method|getEndA ()
specifier|public
specifier|abstract
name|int
name|getEndA
parameter_list|()
function_decl|;
DECL|method|getBeginB ()
specifier|public
specifier|abstract
name|int
name|getBeginB
parameter_list|()
function_decl|;
DECL|method|getEndB ()
specifier|public
specifier|abstract
name|int
name|getEndB
parameter_list|()
function_decl|;
comment|// Used for equals(), for which this value is important.
DECL|method|isImplicitRename ()
specifier|public
specifier|abstract
name|boolean
name|isImplicitRename
parameter_list|()
function_decl|;
DECL|method|toEdit ()
specifier|public
name|Optional
argument_list|<
name|Edit
argument_list|>
name|toEdit
parameter_list|()
block|{
if|if
condition|(
name|getBeginA
argument_list|()
operator|<
literal|0
condition|)
block|{
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|Edit
argument_list|(
name|getBeginA
argument_list|()
argument_list|,
name|getEndA
argument_list|()
argument_list|,
name|getBeginB
argument_list|()
argument_list|,
name|getEndB
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|interface|SideStrategy
specifier|private
interface|interface
name|SideStrategy
block|{
DECL|method|getFilePath (ContextAwareEdit edit)
name|String
name|getFilePath
parameter_list|(
name|ContextAwareEdit
name|edit
parameter_list|)
function_decl|;
DECL|method|getBegin (ContextAwareEdit edit)
name|int
name|getBegin
parameter_list|(
name|ContextAwareEdit
name|edit
parameter_list|)
function_decl|;
DECL|method|getEnd (ContextAwareEdit edit)
name|int
name|getEnd
parameter_list|(
name|ContextAwareEdit
name|edit
parameter_list|)
function_decl|;
DECL|method|create (ContextAwareEdit edit, int shiftedAmount, String adjustedFilePath)
name|ContextAwareEdit
name|create
parameter_list|(
name|ContextAwareEdit
name|edit
parameter_list|,
name|int
name|shiftedAmount
parameter_list|,
name|String
name|adjustedFilePath
parameter_list|)
function_decl|;
block|}
DECL|enum|SideAStrategy
specifier|private
enum|enum
name|SideAStrategy
implements|implements
name|SideStrategy
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|getFilePath (ContextAwareEdit edit)
specifier|public
name|String
name|getFilePath
parameter_list|(
name|ContextAwareEdit
name|edit
parameter_list|)
block|{
return|return
name|edit
operator|.
name|getOldFilePath
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getBegin (ContextAwareEdit edit)
specifier|public
name|int
name|getBegin
parameter_list|(
name|ContextAwareEdit
name|edit
parameter_list|)
block|{
return|return
name|edit
operator|.
name|getBeginA
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getEnd (ContextAwareEdit edit)
specifier|public
name|int
name|getEnd
parameter_list|(
name|ContextAwareEdit
name|edit
parameter_list|)
block|{
return|return
name|edit
operator|.
name|getEndA
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|create ( ContextAwareEdit edit, int shiftedAmount, String adjustedFilePath)
specifier|public
name|ContextAwareEdit
name|create
parameter_list|(
name|ContextAwareEdit
name|edit
parameter_list|,
name|int
name|shiftedAmount
parameter_list|,
name|String
name|adjustedFilePath
parameter_list|)
block|{
return|return
name|ContextAwareEdit
operator|.
name|create
argument_list|(
name|adjustedFilePath
argument_list|,
name|edit
operator|.
name|getNewFilePath
argument_list|()
argument_list|,
name|edit
operator|.
name|getBeginA
argument_list|()
operator|+
name|shiftedAmount
argument_list|,
name|edit
operator|.
name|getEndA
argument_list|()
operator|+
name|shiftedAmount
argument_list|,
name|edit
operator|.
name|getBeginB
argument_list|()
argument_list|,
name|edit
operator|.
name|getEndB
argument_list|()
argument_list|,
operator|!
name|Objects
operator|.
name|equals
argument_list|(
name|edit
operator|.
name|getOldFilePath
argument_list|()
argument_list|,
name|adjustedFilePath
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|enum|SideBStrategy
specifier|private
enum|enum
name|SideBStrategy
implements|implements
name|SideStrategy
block|{
DECL|enumConstant|INSTANCE
name|INSTANCE
block|;
annotation|@
name|Override
DECL|method|getFilePath (ContextAwareEdit edit)
specifier|public
name|String
name|getFilePath
parameter_list|(
name|ContextAwareEdit
name|edit
parameter_list|)
block|{
return|return
name|edit
operator|.
name|getNewFilePath
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getBegin (ContextAwareEdit edit)
specifier|public
name|int
name|getBegin
parameter_list|(
name|ContextAwareEdit
name|edit
parameter_list|)
block|{
return|return
name|edit
operator|.
name|getBeginB
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getEnd (ContextAwareEdit edit)
specifier|public
name|int
name|getEnd
parameter_list|(
name|ContextAwareEdit
name|edit
parameter_list|)
block|{
return|return
name|edit
operator|.
name|getEndB
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|create ( ContextAwareEdit edit, int shiftedAmount, String adjustedFilePath)
specifier|public
name|ContextAwareEdit
name|create
parameter_list|(
name|ContextAwareEdit
name|edit
parameter_list|,
name|int
name|shiftedAmount
parameter_list|,
name|String
name|adjustedFilePath
parameter_list|)
block|{
return|return
name|ContextAwareEdit
operator|.
name|create
argument_list|(
name|edit
operator|.
name|getOldFilePath
argument_list|()
argument_list|,
name|adjustedFilePath
argument_list|,
name|edit
operator|.
name|getBeginA
argument_list|()
argument_list|,
name|edit
operator|.
name|getEndA
argument_list|()
argument_list|,
name|edit
operator|.
name|getBeginB
argument_list|()
operator|+
name|shiftedAmount
argument_list|,
name|edit
operator|.
name|getEndB
argument_list|()
operator|+
name|shiftedAmount
argument_list|,
operator|!
name|Objects
operator|.
name|equals
argument_list|(
name|edit
operator|.
name|getNewFilePath
argument_list|()
argument_list|,
name|adjustedFilePath
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit
